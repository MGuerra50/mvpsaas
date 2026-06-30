import { Link } from "react-router-dom";

import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { cn } from "@/lib/utils";

const plans = [
  {
    name: "Básico",
    price: "R$ 49",
    period: "/mês",
    features: ["Até 2 usuários", "CRM de clientes", "50 orçamentos/mês"],
    highlighted: false,
  },
  {
    name: "Pro",
    price: "R$ 99",
    period: "/mês",
    features: [
      "Usuários ilimitados",
      "Dashboard + PDV WhatsApp",
      "Orçamentos ilimitados",
      "Suporte prioritário",
    ],
    highlighted: true,
  },
  {
    name: "Enterprise",
    price: "Sob consulta",
    period: "",
    features: ["Multi-filial", "API dedicada", "SLA customizado", "Onboarding assistido"],
    highlighted: false,
  },
];

export function LandingPricing() {
  return (
    <section className="py-16">
      <div className="mx-auto max-w-6xl px-6">
        <h2 className="text-center text-3xl font-bold">Planos para cada fase do seu negócio</h2>
        <div className="mt-10 grid gap-6 md:grid-cols-3">
          {plans.map((plan) => (
            <Card
              key={plan.name}
              className={cn(
                plan.highlighted && "border-primary shadow-lg ring-2 ring-primary/20",
              )}
            >
              <CardHeader>
                <CardTitle className="flex items-center justify-between">
                  {plan.name}
                  {plan.highlighted && (
                    <span className="rounded-full bg-primary px-2 py-0.5 text-xs text-primary-foreground">
                      Mais popular
                    </span>
                  )}
                </CardTitle>
                <p className="text-3xl font-bold">
                  {plan.price}
                  <span className="text-base font-normal text-muted-foreground">{plan.period}</span>
                </p>
              </CardHeader>
              <CardContent className="space-y-4">
                <ul className="space-y-2 text-sm text-muted-foreground">
                  {plan.features.map((feature) => (
                    <li key={feature}>• {feature}</li>
                  ))}
                </ul>
                <Button className="w-full" variant={plan.highlighted ? "default" : "outline"} asChild>
                  <Link to="/login">Começar Grátis</Link>
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </section>
  );
}
