import { Quote } from "lucide-react";

import { Card, CardContent } from "@/components/ui/card";

export function LandingTestimonial() {
  return (
    <section className="bg-muted/40 py-16">
      <div className="mx-auto max-w-3xl px-6">
        <Card>
          <CardContent className="p-8 text-center">
            <Quote className="mx-auto h-8 w-8 text-primary" />
            <blockquote className="mt-4 text-lg italic text-foreground">
              &ldquo;Com o OrçaZap passamos a responder orçamentos em menos de 3 minutos. Nosso
              faturamento subiu de forma consistente no primeiro trimestre.&rdquo;
            </blockquote>
            <p className="mt-4 font-semibold">Mônica Pereira</p>
            <p className="text-sm text-muted-foreground">Diretora Comercial, Loja Alpha</p>
          </CardContent>
        </Card>
      </div>
    </section>
  );
}
