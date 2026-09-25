# Parity gate: verifies that classes shared by the client and server source
# trees contain identical code. Run before exporting/deploying either tree.
#
# Usage:  powershell -ExecutionPolicy Bypass -File Main\parity.ps1
#
# Exit code 0  -> parity holds (modulo the allow-listed exceptions below).
# Exit code 1  -> drift detected; offenders are printed.

$Root = Split-Path -Parent $PSScriptRoot
$Client = Join-Path $Root "Main\src\minecraft\net\minecraft"
$Server = Join-Path $Root "Main\src\minecraft_server\net\minecraft"

# Intentional client/server asymmetries. Each entry is the path relative to
# net\minecraft\ in both trees.
$AllowList = @(
    "\network\NetworkManager.java",       # overflow cap (1MB client / 4MB server), drain (1000/100), server-only countQueuedPackets()
    "\world\level\World.java"             # entitySimulationRadiusChunks (8 client / 3 server)
)

$Offenders = @()
Get-ChildItem -Recurse -Filter *.java $Client | ForEach-Object {
    $rel = $_.FullName.Substring($Client.Length)
    $twin = Join-Path $Server $rel
    if (Test-Path $twin) {
        $a = [System.IO.File]::ReadAllBytes($_.FullName)
        $b = [System.IO.File]::ReadAllBytes($twin)
        if (-not [System.Linq.Enumerable]::SequenceEqual($a, $b)) {
            if ($AllowList -notcontains $rel) {
                $Offenders += $rel
            }
        }
    }
}

if ($Offenders.Count -eq 0) {
    Write-Output "PARITY OK ($($AllowList.Count) allow-listed exception(s))"
    exit 0
}

Write-Output ("PARITY BROKEN - " + $Offenders.Count + " file(s) differ:")
$Offenders | ForEach-Object { Write-Output ("  " + $_.Substring(1)) }
exit 1