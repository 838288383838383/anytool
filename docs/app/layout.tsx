import type { Metadata } from 'next'
import './globals.css'
import DarkModeToggle from '@/components/DarkModeToggle'

export const metadata: Metadata = {
  title: 'AnyTool — Android Multi-Tool Documentation',
  description: 'Documentation, tutorials, and guides for AnyTool — the Android multi-tool powered by Shizuku/Dhizuku.',
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className="min-h-screen">
        <nav className="sticky top-0 z-50 border-b border-[var(--border)] bg-[var(--surface)]/80 backdrop-blur-xl">
          <div className="mx-auto flex h-16 max-w-6xl items-center justify-between px-4">
            <a href="/" className="flex items-center gap-2 text-xl font-bold">
              <span className="text-2xl">🔧</span> AnyTool
            </a>
            <div className="flex items-center gap-6 text-sm font-medium">
              <a href="/docs" className="hover:text-[var(--brand)]">Docs</a>
              <a href="/docs/guide/page-01" className="hover:text-[var(--brand)]">Guide</a>
              <a href="/tutorials" className="hover:text-[var(--brand)]">Tutorials</a>
              <a href="/qa" className="hover:text-[var(--brand)]">Q&amp;A</a>
              <a href="/community" className="hover:text-[var(--brand)]">Community</a>
              <a href="https://github.com/838288383838383/anytool" target="_blank" className="hover:text-[var(--brand)]">GitHub</a>
              <DarkModeToggle />
            </div>
          </div>
        </nav>
        <main>{children}</main>
        <footer className="border-t border-[var(--border)] py-8 text-center text-sm text-[var(--muted)]">
          <p>AnyTool &copy; 2024 AnyTool Contributors &mdash; Apache 2.0 License</p>
          <p className="mt-2">Built with Shizuku, Dhizuku, and ❤️</p>
        </footer>
      </body>
    </html>
  )
}
