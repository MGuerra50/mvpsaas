import { Link } from "react-router-dom";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";

export function LandingHero() {
  return (
    <section className="mx-auto max-w-6xl px-6 py-16 lg:py-24">
      <div className="grid items-center gap-12 lg:grid-cols-2">
        <div>
          <h1 className="text-4xl font-bold tracking-tight lg:text-5xl">
            Orçamentos B2B que viram vendas em minutos
          </h1>
          <p className="mt-4 text-lg text-muted-foreground">
            CRM, dashboard e PDV integrados. Monte propostas, aprove e envie pelo WhatsApp sem
            complicação — tudo em um só lugar.
          </p>
          <Button className="mt-8 h-12 px-8 text-base" asChild>
            <Link to="/login">Começar Grátis</Link>
          </Button>
        </div>
        <div className="perspective-[1200px]">
          <Card className="rotate-y-[-8deg] rotate-x-[6deg] shadow-2xl shadow-primary/20 transition-transform hover:rotate-y-[-4deg] hover:rotate-x-[3deg]">
            <CardContent className="space-y-4 p-6">
              <div className="flex items-center justify-between">
                <span className="font-semibold text-primary">OrçaZap</span>
                <span className="text-xs text-muted-foreground">Dashboard</span>
              </div>
              <div className="rounded-md bg-muted p-4">
                <p className="text-xs text-muted-foreground">Faturamento Total Bruto</p>
                <p className="text-2xl font-bold">R$ 12.450,00</p>
              </div>
              <div className="grid grid-cols-4 gap-2">
                {[40, 65, 45, 80].map((height, index) => (
                  <div
                    key={index}
                    className="rounded-sm bg-primary/80"
                    style={{ height: `${height}px` }}
                  />
                ))}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </section>
  );
}
