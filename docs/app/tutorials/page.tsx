export default function Tutorials() {
  const tutorials = [
    { title: 'Advanced Debloating Strategies', desc: 'Deep dive into OEM-specific debloat approaches, dependency mapping, and recovery procedures.', href: '/tutorials/advanced-debloat' },
    { title: 'Linux Sandbox Setup Guide', desc: 'Complete guide to setting up proot-distro with all 6 distributions.', href: '/tutorials/linux-sandbox-setup' },
    { title: 'Creating Custom Themes', desc: 'Design your own theme with custom colors, shapes, and Material 3 properties.', href: '/tutorials/custom-themes' },
    { title: 'Icon Design Workshop', desc: 'Master the Icon Customizer with layer composition, overlays, and JSON design.', href: '/tutorials/icon-designs' },
    { title: 'Shell Automation', desc: 'Automate Android tasks with shell commands, scripts, and Tasker integration.', href: '/tutorials/automation' },
  ]
  return (
    <div className="mx-auto max-w-4xl px-4 py-12">
      <h1 className="text-4xl font-bold mb-8">Tutorials</h1>
      <p className="text-[var(--muted)] mb-12">In-depth guides for power users.</p>
      <div className="space-y-6">
        {tutorials.map(t => (
          <a key={t.title} href={t.href} className="block rounded-2xl border border-[var(--border)] bg-[var(--surface)] p-6 hover:border-[var(--brand)] transition-colors">
            <h2 className="text-xl font-semibold">{t.title}</h2>
            <p className="mt-2 text-[var(--muted)]">{t.desc}</p>
          </a>
        ))}
      </div>
    </div>
  )
}
