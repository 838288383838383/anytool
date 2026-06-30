export default function Home() {
  const features = [
    { icon: '🗑️', title: 'Debloater', desc: 'Remove bloatware with Normal, ZeroDay, and Brute modes' },
    { icon: '⚙️', title: 'System Tweaks', desc: 'Animations, DPI, immersive mode, performance tuning' },
    { icon: '📝', title: 'Build Prop Editor', desc: 'View and edit system build properties' },
    { icon: '🔐', title: 'Permission Manager', desc: 'Grant/revoke permissions for any app' },
    { icon: '📊', title: 'Battery Stats', desc: 'Health, temperature, voltage, and more' },
    { icon: '🌐', title: 'Network Info', desc: 'IP, WiFi, DNS, public IP, carrier info' },
    { icon: '📱', title: 'App Manager', desc: 'Force stop, disable, clear data, uninstall' },
    { icon: '⚡', title: 'Process Manager', desc: 'View and kill running processes' },
    { icon: '💻', title: 'Shell Terminal', desc: 'ADB, rish, and Linux Sandbox modes' },
    { icon: '🐧', title: 'Linux Sandbox', desc: 'Debian, Ubuntu, Arch, Gentoo, openSUSE, NixOS' },
    { icon: '📡', title: 'WiFi Hotspot', desc: 'One-switch WiFi sharing, no root needed' },
    { icon: '🎨', title: 'Icon Customizer', desc: 'Design custom icons, import community JSON' },
    { icon: '🎵', title: 'Spotify Player', desc: 'Play offline tracks from SD card' },
    { icon: '🔊', title: 'Volume Manager', desc: 'Accessibility shortcuts via volume buttons' },
    { icon: '🚀', title: 'App Launcher', desc: 'Themed app grid with 9 theme options' },
    { icon: '📋', title: 'Logcat Viewer', desc: 'Real-time system logs with filters' },
  ]

  return (
    <div>
      <section className="relative overflow-hidden py-24 text-center">
        <div className="absolute inset-0 bg-gradient-to-br from-brand-500/10 to-transparent" />
        <div className="relative mx-auto max-w-4xl px-4">
          <h1 className="text-5xl font-bold tracking-tight md:text-7xl">
            Any<span className="text-[var(--brand)]">Tool</span>
          </h1>
          <p className="mt-6 text-xl text-[var(--muted)]">Android Multi-Tool powered by Shizuku & Dhizuku</p>
          <p className="mt-4 text-lg text-[var(--muted)]">Debloat. Tweak. Debug. Explore. No root required.</p>
          <div className="mt-8 flex justify-center gap-4">
            <a href="/docs/guide/page-01" className="rounded-xl bg-[var(--brand)] px-8 py-3 text-white font-semibold hover:opacity-90 transition">Get Started</a>
            <a href="https://github.com/838288383838383/anytool" target="_blank" className="rounded-xl border border-[var(--border)] px-8 py-3 font-semibold hover:bg-[var(--border)] transition">GitHub</a>
          </div>
        </div>
      </section>

      <section className="mx-auto max-w-6xl px-4 py-16">
        <h2 className="text-3xl font-bold text-center mb-12">Everything You Need</h2>
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
          {features.map((f) => (
            <div key={f.title} className="rounded-2xl border border-[var(--border)] bg-[var(--surface)] p-6 hover:border-[var(--brand)] transition-colors">
              <div className="text-3xl mb-3">{f.icon}</div>
              <h3 className="font-semibold text-lg">{f.title}</h3>
              <p className="mt-1 text-sm text-[var(--muted)]">{f.desc}</p>
            </div>
          ))}
        </div>
      </section>

      <section className="border-t border-[var(--border)] py-16 text-center">
        <h2 className="text-3xl font-bold">Ready to take control?</h2>
        <p className="mt-4 text-[var(--muted)]">Download AnyTool and start customizing your Android experience.</p>
        <a href="https://github.com/838288383838383/anytool/releases" className="mt-6 inline-block rounded-xl bg-[var(--brand)] px-8 py-3 text-white font-semibold hover:opacity-90 transition">Download APK</a>
      </section>
    </div>
  )
}
