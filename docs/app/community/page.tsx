export default function Community() {
  return (
    <div className="mx-auto max-w-4xl px-4 py-12">
      <h1 className="text-4xl font-bold mb-8">Community</h1>

      <section className="mb-12">
        <h2 className="text-2xl font-bold mb-4">Get Involved</h2>
        <div className="grid gap-6 sm:grid-cols-2">
          <a href="https://github.com/838288383838383/anytool" target="_blank" className="rounded-2xl border border-[var(--border)] bg-[var(--surface)] p-6 hover:border-[var(--brand)] transition-colors">
            <h3 className="font-semibold text-lg">GitHub Repository</h3>
            <p className="mt-2 text-sm text-[var(--muted)]">Source code, issues, and releases</p>
          </a>
          <a href="https://github.com/838288383838383/anytool/issues" target="_blank" className="rounded-2xl border border-[var(--border)] bg-[var(--surface)] p-6 hover:border-[var(--brand)] transition-colors">
            <h3 className="font-semibold text-lg">Report Issues</h3>
            <p className="mt-2 text-sm text-[var(--muted)]">Found a bug? Let us know</p>
          </a>
          <a href="https://github.com/838288383838383/anytool/pulls" target="_blank" className="rounded-2xl border border-[var(--border)] bg-[var(--surface)] p-6 hover:border-[var(--brand)] transition-colors">
            <h3 className="font-semibold text-lg">Pull Requests</h3>
            <p className="mt-2 text-sm text-[var(--muted)]">Contribute code and features</p>
          </a>
          <a href="https://github.com/838288383838383/anytool/discussions" target="_blank" className="rounded-2xl border border-[var(--border)] bg-[var(--surface)] p-6 hover:border-[var(--brand)] transition-colors">
            <h3 className="font-semibold text-lg">Discussions</h3>
            <p className="mt-2 text-sm text-[var(--muted)]">Ask questions, share ideas</p>
          </a>
        </div>
      </section>

      <section className="mb-12">
        <h2 className="text-2xl font-bold mb-4">Contributing</h2>
        <ol className="space-y-4 text-[var(--muted)]">
          <li><strong className="text-[var(--fg)]">1. Fork the repo</strong> — Click Fork on GitHub</li>
          <li><strong className="text-[var(--fg)]">2. Clone your fork</strong> — <code className="rounded bg-[var(--border)] px-2 py-1">git clone https://github.com/YOUR_USERNAME/anytool.git</code></li>
          <li><strong className="text-[var(--fg)]">3. Create a branch</strong> — <code className="rounded bg-[var(--border)] px-2 py-1">git checkout -b feature/my-feature</code></li>
          <li><strong className="text-[var(--fg)]">4. Make changes</strong> — Edit code, add features, fix bugs</li>
          <li><strong className="text-[var(--fg)]">5. Test</strong> — Build and verify on a real device</li>
          <li><strong className="text-[var(--fg)]">6. Push and PR</strong> — Push to your fork and open a Pull Request</li>
        </ol>
      </section>

      <section>
        <h2 className="text-2xl font-bold mb-4">Share Your Designs</h2>
        <p className="text-[var(--muted)] mb-4">
          Created a custom icon with the Icon Customizer? Share it with the community!
        </p>
        <ol className="space-y-2 text-[var(--muted)]">
          <li>1. Export your design as JSON</li>
          <li>2. Create a GitHub Gist or add to the community designs repo</li>
          <li>3. Share the link in Discussions</li>
        </ol>
      </section>
    </div>
  )
}
