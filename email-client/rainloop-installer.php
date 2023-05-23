#!/usr/bin/env php
<?php

define('INSTALLER_ROOT_PATH', rtrim(dirname(__FILE__), '\\/').'/');
define('INSTALLER_REPO_CORE', 'https://www.rainloop.net/repository/webmail/rainloop-latest.zip');
define('INSTALLER_REPO_SPEC', 'https://repository.rainloop.net/spec/');
define('INSTALLER_RND', md5(microtime(true)));

out('');
out('');
out('       [RainLoop Webmail Installer]');
out('');
out('');

if (is_dir(INSTALLER_ROOT_PATH.'rainloop') || is_dir(INSTALLER_ROOT_PATH.'data') || file_exists(INSTALLER_ROOT_PATH.'index.php'))
{
	out('[Error] Directory is not empty! Please re-install, specifying an empty (or new) directory.', true);
}

testPHP();
testCurrentFolder();

out('Connecting to repository ...');

// get latest RainLoop Webmail package file
if (!getPacker() || !getPackage() ||
	!file_exists(INSTALLER_ROOT_PATH.INSTALLER_RND.'/'.INSTALLER_RND.'.rlp') ||
	!file_exists(INSTALLER_ROOT_PATH.INSTALLER_RND.'/'.INSTALLER_RND.'.lib'))
{
	out('[Error] Cannot connect to repository', true);
}

include_once INSTALLER_ROOT_PATH.INSTALLER_RND.'/'.INSTALLER_RND.'.lib';

if (!class_exists('PclZip'))
{
	out('[Error] Cannot connect to repository', true);
}

out('Installing package ...');
$oArchive = new PclZip(INSTALLER_ROOT_PATH.INSTALLER_RND.'/'.INSTALLER_RND.'.rlp');
$bResult = 0 !== $oArchive->extract(PCLZIP_OPT_PATH, INSTALLER_ROOT_PATH);
if (
	!$bResult ||
	!file_exists(INSTALLER_ROOT_PATH.'index.php') ||
	!is_dir(INSTALLER_ROOT_PATH.'rainloop/')
)
{
	out('[Error] Cannot unzip package', true);
}

out('Complete installing!');

chmod_R(INSTALLER_ROOT_PATH.'data', 0666, 0777);
chmod_R(INSTALLER_ROOT_PATH.'rainloop', 0644, 0755);

@chmod(INSTALLER_ROOT_PATH.'rainloop/', 0777);
@chmod(INSTALLER_ROOT_PATH.'rainloop/v/', 0777);
@chmod(INSTALLER_ROOT_PATH.'index.php', 0666);

out('');
out('[Success] Installation is finished!');
out('');
endScript(0);

/* Functions */

function endScript($iErrorCode = 0)
{
	@unlink(INSTALLER_ROOT_PATH.INSTALLER_RND.'/'.INSTALLER_RND.'.lib');
	@unlink(INSTALLER_ROOT_PATH.INSTALLER_RND.'/'.INSTALLER_RND.'.rlp');
	@unlink(INSTALLER_ROOT_PATH.INSTALLER_RND.'/'.INSTALLER_RND.'.file');
	@rmdir(INSTALLER_ROOT_PATH.INSTALLER_RND);

	exit($iErrorCode);
}

function out($sDesc, $bExitWithErrorCode = false)
{
	if (0 < strlen($sDesc) && ' ' !== $sDesc[0] )
	{
		echo ' * ';
	}

	echo $sDesc;
	echo "\r\n";

	if ($bExitWithErrorCode)
	{
		endScript(1);
	}
}

function testPHP()
{
	if (0 > version_compare(PHP_VERSION, '5.4.0'))
	{
		out('[Error] Your PHP version ('.PHP_VERSION.') is lower than the minimal required 5.4.0!', true);
	}

	$aRequirements = array(
		'cURL' => function_exists('curl_init'),
		'iconv' => function_exists('iconv'),
		'json' => function_exists('json_decode'),
		'DateTime' => class_exists('DateTime') && class_exists('DateTimeZone'),
		'libxml' => function_exists('libxml_use_internal_errors'),
		'dom' => class_exists('DOMDocument'),
		'Zlib' => function_exists('gzopen') || function_exists('gzopen64'),
		'PCRE' => function_exists('preg_replace'),
		'SPL' => function_exists('spl_autoload_register')
	);

	if (in_array(false, $aRequirements))
	{
		out('[Error] The following PHP extensions are not available in your PHP configuration!');

		$aLine = array();
		foreach ($aRequirements as $sKey => $bValue)
		{
			if (!$bValue)
			{
				$aLine[] = $sKey;
			}
		}

		out(implode(', ', $aLine), true);
	}
}

function testCurrentFolder()
{
	$bResult = @mkdir(INSTALLER_ROOT_PATH.INSTALLER_RND);
	if ($bResult)
	{
		$bResult = false !== @file_put_contents(INSTALLER_ROOT_PATH.INSTALLER_RND.'/'.INSTALLER_RND.'.file', INSTALLER_RND);
		if (!$bResult)
		{
			out('[Error] Cannot save file', true);
		}
	}
	else
	{
		out('[Error] Cannot save folder', true);
	}
}

function getPackage()
{
	$bResult = false;

	out('Downloading package ...');

	$pSrc = @fopen(INSTALLER_REPO_CORE, 'rb');
	if ($pSrc)
	{
		$sTmp = INSTALLER_ROOT_PATH.INSTALLER_RND.'/'.INSTALLER_RND.'.rlp';
		$pDest = @fopen($sTmp, 'w+b');
		if ($pDest)
		{
			$iCopy = stream_copy_to_stream($pSrc, $pDest);

			$bResult = is_int($iCopy) && 0 < $iCopy;
			@fclose($pDest);
		}

		@fclose($pSrc);
	}

	out('Complete downloading!');

	return $bResult;
}

function getPacker()
{
	$bResult = false;

	$pSrc = @fopen(INSTALLER_REPO_SPEC.'pclzip/pclzip.lib.php', 'rb');
	if ($pSrc)
	{
		$sTmp = INSTALLER_ROOT_PATH.INSTALLER_RND.'/'.INSTALLER_RND.'.lib';
		$pDest = @fopen($sTmp, 'w+b');
		if ($pDest)
		{
			$iCopy = stream_copy_to_stream($pSrc, $pDest);

			$bResult = is_int($iCopy) && 0 < $iCopy;
			@fclose($pDest);
		}

		@fclose($pSrc);
	}

	return $bResult;
}

function chmod_R($path, $filemode, $dirmode)
{
	if (@is_dir($path))
	{
		@chmod($path, $dirmode);

		$dh = @opendir($path);
		if ($dh)
		{
			while (($file = readdir($dh)) !== false)
			{
				if ($file !== '.' && $file !== '..')
				{
					$fullpath = $path.'/'.$file;
					chmod_R($fullpath, $filemode, $dirmode);
				}
			}

			@closedir($dh);
		}
	}
	else
	{
		@chmod($path, $filemode);
	}
}
