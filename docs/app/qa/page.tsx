export default function QA() {
  const faqs = [
    { q: 'Do I need root to use AnyTool?', a: 'No. Most features work via Shizuku (ADB without root). Only Brute Mode debloat requires root.' },
    { q: 'What is Shizuku?', a: 'Shizuku runs an ADB service on your device, letting apps execute shell commands with elevated privileges without a computer.' },
    { q: 'How do I start Shizuku without root?', a: 'Enable Wireless Debugging in Developer Options, then start Shizuku via wireless debugging pairing.' },
    { q: 'Is it safe to debloat my phone?', a: 'Normal mode is safe — it only disables apps, easily reversible. ZeroDay removes for current user. Brute mode is risky.' },
    { q: 'What happens if I disable a critical system app?', a: 'Your phone may restart or certain features may stop working. Use Backup/Restore to re-enable it.' },
    { q: 'Can I use AnyTool on a non-rooted phone?', a: 'Yes! Shizuku works via wireless debugging. You only miss Brute Mode debloat.' },
    { q: 'Does AnyTool collect data?', a: 'No. AnyTool is fully offline. No analytics, no telemetry, no internet required (except Linux Sandbox via Termux).' },
    { q: 'How do I install proot-distro?', a: 'Install Termux from F-Droid, then run: pkg install proot-distro && proot-distro install debian' },
    { q: 'Why can\'t I see Google Play version of Termux?', a: 'Google Play version is outdated and broken. Use F-Droid or GitHub releases.' },
    { q: 'What Android versions are supported?', a: 'Android 8.0 (API 26) and above. Tested on Android 8-16.' },
    { q: 'How do I report a bug?', a: 'Open an issue at github.com/838288383838383/anytool/issues with device info and steps to reproduce.' },
    { q: 'Can I contribute?', a: 'Yes! Fork the repo, make changes, and open a pull request. See the contributing guide in README.' },
    { q: 'How do I change the app icon?', a: 'Use the Icon Customizer tool. Design your icon, export as JSON, or import community designs.' },
    { q: 'What Linux distro should I start with?', a: 'Debian for stability, Ubuntu for familiarity, Arch for bleeding-edge packages.' },
    { q: 'Does the WiFi hotspot require root?', a: 'No. AnyTool uses svc wifi enablehotspot which works without root on most devices.' },
  ]

  return (
    <div className="mx-auto max-w-4xl px-4 py-12">
      <h1 className="text-4xl font-bold mb-4">Frequently Asked Questions</h1>
      <p className="text-[var(--muted)] mb-12">{faqs.length} questions answered</p>
      <div className="space-y-6">
        {faqs.map((f, i) => (
          <details key={i} className="group rounded-2xl border border-[var(--border)] bg-[var(--surface)] overflow-hidden">
            <summary className="flex cursor-pointer items-center justify-between p-6 font-semibold text-lg hover:bg-[var(--border)]/30 transition">
              {f.q}
              <span className="ml-4 text-[var(--muted)] group-open:rotate-180 transition-transform">▼</span>
            </summary>
            <div className="px-6 pb-6 text-[var(--muted)]">{f.a}</div>
          </details>
        ))}
      </div>
    </div>
  )
}
