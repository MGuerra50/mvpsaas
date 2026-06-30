export function LandingFooter() {
  const year = new Date().getFullYear();

  return (
    <footer className="bg-zinc-900 py-10 text-zinc-300">
      <div className="mx-auto max-w-6xl px-6 text-center">
        <p className="text-lg font-semibold text-white">OrçaZap</p>
        <p className="mt-2 text-sm">Orçamentos B2B com CRM, dashboard e WhatsApp integrado.</p>
        <p className="mt-6 text-xs text-zinc-500">
          © {year} OrçaZap. Todos os direitos reservados.
        </p>
      </div>
    </footer>
  );
}
