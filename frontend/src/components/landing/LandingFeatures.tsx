import { CreditCard, Shield } from "lucide-react";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

const features = [
  {
    icon: Shield,
    title: "Arquitetura Multi-Tenant Segura",
    description:
      "Isolamento por tenant com JWT e filtros Hibernate. Seus dados e os de cada cliente ficam protegidos.",
  },
  {
    icon: CreditCard,
    title: "Checkout Integrado Pix e Cartão",
    description:
      "Fluxo de pagamento pensado para conversão: orçamento aprovado vira cobrança com Pix e cartão em um clique.",
  },
];

export function LandingFeatures() {
  return (
    <section className="bg-muted/40 py-16">
      <div className="mx-auto max-w-6xl px-6">
        <h2 className="text-center text-3xl font-bold">Recursos que aceleram suas vendas</h2>
        <div className="mt-10 grid gap-6 md:grid-cols-2">
          {features.map(({ icon: Icon, title, description }) => (
            <Card key={title}>
              <CardHeader>
                <div className="mb-2 flex h-10 w-10 items-center justify-center rounded-md bg-primary/10">
                  <Icon className="h-5 w-5 text-primary" />
                </div>
                <CardTitle>{title}</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-muted-foreground">{description}</p>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    </section>
  );
}
