param([string]$ProjectRoot=(Split-Path $PSScriptRoot -Parent))
$ErrorActionPreference='Stop'
$browser=Join-Path $env:ProgramFiles 'Google/Chrome/Application/chrome.exe'
if (-not (Test-Path $browser)) { $browser=Join-Path ${env:ProgramFiles(x86)} 'Microsoft/Edge/Application/msedge.exe' }
if (-not (Test-Path $browser)) { throw 'Chrome or Edge is required to print the report.' }
$target=Join-Path $ProjectRoot 'B24DCCC049_TranHaiDang_BaoCao.pdf'
$profile=Join-Path $ProjectRoot 'report-qa/chrome-profile'
$html=[Uri](Join-Path $ProjectRoot 'docs/report.html')
& $browser --headless --disable-gpu --no-first-run --no-pdf-header-footer "--user-data-dir=$profile" "--print-to-pdf=$target" $html.AbsoluteUri
if (-not (Test-Path $target)) { throw 'The PDF was not created.' }
Write-Output $target
