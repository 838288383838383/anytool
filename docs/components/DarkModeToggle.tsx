'use client'

export default function DarkModeToggle() {
  return (
    <button
      onClick={() => {
        document.documentElement.classList.toggle('dark')
        localStorage.setItem('theme', document.documentElement.classList.contains('dark') ? 'dark' : 'light')
      }}
      className="rounded-lg border border-[var(--border)] px-3 py-1 hover:bg-[var(--border)]"
    >
      🌓
    </button>
  )
}
