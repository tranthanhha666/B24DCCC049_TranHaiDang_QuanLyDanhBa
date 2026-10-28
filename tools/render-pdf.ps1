param([string]$ProjectRoot)
$ErrorActionPreference='Stop'
Add-Type -AssemblyName System.Runtime.WindowsRuntime
[Windows.Storage.StorageFile,Windows.Storage,ContentType=WindowsRuntime] | Out-Null
[Windows.Data.Pdf.PdfDocument,Windows.Data.Pdf,ContentType=WindowsRuntime] | Out-Null
[Windows.Storage.Streams.InMemoryRandomAccessStream,Windows.Storage.Streams,ContentType=WindowsRuntime] | Out-Null
[Windows.Storage.Streams.DataReader,Windows.Storage.Streams,ContentType=WindowsRuntime] | Out-Null
[Windows.Data.Pdf.PdfPageRenderOptions,Windows.Data.Pdf,ContentType=WindowsRuntime] | Out-Null
$asTask=([System.WindowsRuntimeSystemExtensions].GetMethods() | Where-Object { $_.Name -eq 'AsTask' -and $_.IsGenericMethod -and $_.GetGenericArguments().Count -eq 1 -and $_.GetParameters().Count -eq 1 -and $_.GetParameters()[0].ParameterType.Name -eq 'IAsyncOperation`1' })[0]
function Await($Operation,[Type]$ResultType) {
 $task=$asTask.MakeGenericMethod($ResultType).Invoke($null,@($Operation))
 $task.Wait()
 $task.Result
}
$actionAsTask=([System.WindowsRuntimeSystemExtensions].GetMethods() | Where-Object { $_.Name -eq 'AsTask' -and -not $_.IsGenericMethod -and $_.GetParameters().Count -eq 1 -and $_.GetParameters()[0].ParameterType.Name -eq 'IAsyncAction' })[0]
$path=Join-Path $ProjectRoot 'B24DCCC049_TranHaiDang_BaoCao.pdf'
$file=Await ([Windows.Storage.StorageFile]::GetFileFromPathAsync($path)) ([Windows.Storage.StorageFile])
$pdf=Await ([Windows.Data.Pdf.PdfDocument]::LoadFromFileAsync($file)) ([Windows.Data.Pdf.PdfDocument])
$qa=Join-Path $ProjectRoot 'report-qa'
New-Item -ItemType Directory -Force $qa | Out-Null
Write-Output ('Pages: '+$pdf.PageCount)
for ($i=0;$i -lt $pdf.PageCount;$i++) {
 $page=$pdf.GetPage($i)
 $stream=New-Object Windows.Storage.Streams.InMemoryRandomAccessStream
 $options=New-Object Windows.Data.Pdf.PdfPageRenderOptions
 $options.DestinationWidth=1275
 $options.DestinationHeight=1650
 $actionAsTask.Invoke($null,@($page.RenderToStreamAsync($stream,$options))).Wait()
 $reader=New-Object Windows.Storage.Streams.DataReader($stream.GetInputStreamAt(0))
 $null=Await ($reader.LoadAsync([uint32]$stream.Size)) ([uint32])
 $bytes=New-Object byte[] ([int]$stream.Size)
 $reader.ReadBytes($bytes)
 $target=Join-Path $qa ('page-'+($i+1)+'.png')
 [IO.File]::WriteAllBytes($target,$bytes)
 $reader.Dispose(); $stream.Dispose(); $page.Dispose()
 Write-Output $target
}
