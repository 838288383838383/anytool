const pages = Array.from({length: 20}, (_, i) => ({
  num: String(i+1).padStart(2, '0'),
  titles: [
    'Introduction & Overview', 'Installation & Setup', 'Shizuku Configuration',
    'Debloater — Normal Mode', 'Debloater — ZeroDay & Brute', 'System Tweaks',
    'Build Prop Editor', 'Permission Manager', 'Process & App Manager',
    'Battery & Network Info', 'Shell Terminal — ADB & rish', 'Linux Sandbox (proot-distro)',
    'Logcat Viewer', 'Stress Testing', 'WiFi Hotspot',
    'Spotify Offline Player', 'Volume Manager', 'App Launcher & Themes',
    'Icon Customizer', 'Backup, Restore & Settings'
  ][i]
}))

export default function DocsLayout({ children }: { children: React.ReactNode }) {
  return (
    <div className="mx-auto flex max-w-6xl gap-8 px-4 py-8">
      <aside className="hidden w-64 shrink-0 md:block">
        <nav className="sticky top-24 space-y-1">
          <a href="/docs" className="block rounded-lg px-3 py-2 text-sm font-semibold hover:bg-[var(--surface)]">Overview</a>
          <p className="mt-4 px-3 text-xs font-bold uppercase text-[var(--muted)] tracking-wider">20-Page Guide</p>
          {pages.map(p => (
            <a key={p.num} href={`/docs/guide/page-${p.num}`} className="block rounded-lg px-3 py-1.5 text-sm hover:bg-[var(--surface)]">
              {p.num}. {p.titles}
            </a>
          ))}
          <p className="mt-4 px-3 text-xs font-bold uppercase text-[var(--muted)] tracking-wider">More</p>
          <a href="/tutorials" className="block rounded-lg px-3 py-2 text-sm hover:bg-[var(--surface)]">Tutorials</a>
          <a href="/qa" className="block rounded-lg px-3 py-2 text-sm hover:bg-[var(--surface)]">Q&A</a>
          <a href="/community" className="block rounded-lg px-3 py-2 text-sm hover:bg-[var(--surface)]">Community</a>
        </nav>
      </aside>
      <article className="prose prose-lg max-w-none dark:prose-invert flex-1 min-w-0">{children}</article>
    </div>
  )
}
