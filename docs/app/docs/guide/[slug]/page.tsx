import { compileMDX } from 'next-mdx-remote/rsc'
import { readFile, readdir } from 'fs/promises'
import { join } from 'path'
import { notFound } from 'next/navigation'

export async function generateStaticParams() {
  const contentDir = join(process.cwd(), 'content', 'guide')
  const files = await readdir(contentDir)
  return files
    .filter(f => f.endsWith('.mdx'))
    .map(f => ({ slug: f.replace('.mdx', '') }))
}

const components = {}

export default async function GuidePage({ params }: { params: { slug: string } }) {
  const filePath = join(process.cwd(), 'content', 'guide', `${params.slug}.mdx`)
  let source: string
  try {
    source = await readFile(filePath, 'utf-8')
  } catch {
    notFound()
  }

  const { content } = await compileMDX({
    source,
    components,
    options: { mdxOptions: {} },
  })

  return (
    <article className="mx-auto max-w-3xl px-4 py-12 prose prose-invert">
      {content}
    </article>
  )
}
