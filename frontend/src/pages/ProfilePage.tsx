import { zodResolver } from "@hookform/resolvers/zod";
import { Camera, UserCircle2 } from "lucide-react";
import { useEffect, useRef, useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";

import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  changePassword,
  fetchProfile,
  updateProfile,
  type UserProfile,
} from "@/lib/api/profile";

const profileSchema = z.object({
  name: z.string().min(1, "Informe o nome"),
});

const passwordSchema = z
  .object({
    currentPassword: z.string().min(1, "Informe a senha atual"),
    newPassword: z.string().min(6, "A nova senha deve ter ao menos 6 caracteres"),
    confirmPassword: z.string().min(1, "Confirme a nova senha"),
  })
  .refine((data) => data.newPassword === data.confirmPassword, {
    message: "As senhas não coincidem",
    path: ["confirmPassword"],
  });

type ProfileFormValues = z.infer<typeof profileSchema>;
type PasswordFormValues = z.infer<typeof passwordSchema>;

const MAX_IMAGE_BYTES = 5 * 1024 * 1024;

export function ProfilePage() {
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [profileImageUrl, setProfileImageUrl] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [savingProfile, setSavingProfile] = useState(false);
  const [savingPassword, setSavingPassword] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const profileForm = useForm<ProfileFormValues>({
    resolver: zodResolver(profileSchema),
    defaultValues: { name: "" },
  });

  const passwordForm = useForm<PasswordFormValues>({
    resolver: zodResolver(passwordSchema),
    defaultValues: {
      currentPassword: "",
      newPassword: "",
      confirmPassword: "",
    },
  });

  useEffect(() => {
    async function load() {
      setLoading(true);
      setError(null);
      try {
        const data = await fetchProfile();
        setProfile(data);
        setProfileImageUrl(data.profileImageUrl);
        profileForm.reset({ name: data.name });
      } catch {
        setError("Não foi possível carregar seu perfil.");
      } finally {
        setLoading(false);
      }
    }

    load();
  }, [profileForm]);

  async function handleImageChange(event: React.ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0];
    if (!file) return;

    if (!file.type.startsWith("image/")) {
      setError("Selecione um arquivo de imagem válido.");
      return;
    }

    if (file.size > MAX_IMAGE_BYTES) {
      setError("A imagem deve ter no máximo 5 MB.");
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      setProfileImageUrl(typeof reader.result === "string" ? reader.result : null);
      setError(null);
    };
    reader.readAsDataURL(file);
  }

  async function handleProfileSubmit(values: ProfileFormValues) {
    setSavingProfile(true);
    setError(null);
    setSuccess(null);
    try {
      const updated = await updateProfile({
        name: values.name,
        profileImageUrl,
      });
      setProfile(updated);
      setSuccess("Perfil atualizado com sucesso.");
    } catch {
      setError("Não foi possível salvar o perfil.");
    } finally {
      setSavingProfile(false);
    }
  }

  async function handlePasswordSubmit(values: PasswordFormValues) {
    setSavingPassword(true);
    setError(null);
    setSuccess(null);
    try {
      await changePassword({
        currentPassword: values.currentPassword,
        newPassword: values.newPassword,
      });
      passwordForm.reset();
      setSuccess("Senha alterada com sucesso.");
    } catch {
      setError("Não foi possível alterar a senha. Verifique a senha atual.");
    } finally {
      setSavingPassword(false);
    }
  }

  if (loading) {
    return (
      <div className="rounded-lg border border-border bg-card p-8 text-center text-muted-foreground">
        Carregando perfil...
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-2xl space-y-8">
      <div>
        <h1 className="text-2xl font-bold">Meu perfil</h1>
        <p className="text-muted-foreground">Gerencie seus dados pessoais e credenciais de acesso.</p>
      </div>

      {error && <p className="text-sm text-destructive">{error}</p>}
      {success && <p className="text-sm text-emerald-600">{success}</p>}

      <section className="rounded-lg border border-border bg-card p-6 shadow-sm">
        <h2 className="mb-4 text-lg font-semibold">Dados pessoais</h2>
        <Form {...profileForm}>
          <form onSubmit={profileForm.handleSubmit(handleProfileSubmit)} className="space-y-4">
            <div className="flex items-center gap-4">
              <div className="relative">
                {profileImageUrl ? (
                  <img
                    src={profileImageUrl}
                    alt="Foto de perfil"
                    className="h-20 w-20 rounded-full border border-border object-cover"
                  />
                ) : (
                  <div className="flex h-20 w-20 items-center justify-center rounded-full border border-border bg-muted">
                    <UserCircle2 className="h-10 w-10 text-muted-foreground" />
                  </div>
                )}
                <button
                  type="button"
                  onClick={() => fileInputRef.current?.click()}
                  className="absolute -bottom-1 -right-1 rounded-full border border-border bg-background p-1.5 shadow-sm hover:bg-muted"
                  aria-label="Alterar foto de perfil"
                >
                  <Camera className="h-3.5 w-3.5" />
                </button>
                <input
                  ref={fileInputRef}
                  type="file"
                  accept="image/*"
                  className="hidden"
                  onChange={handleImageChange}
                />
              </div>
              <div className="text-sm text-muted-foreground">
                <p>Foto de perfil (opcional)</p>
                <p className="text-xs">PNG ou JPG, até 5 MB</p>
                {profileImageUrl && (
                  <Button
                    type="button"
                    variant="outline"
                    size="sm"
                    className="mt-2"
                    onClick={() => setProfileImageUrl(null)}
                  >
                    Remover foto
                  </Button>
                )}
              </div>
            </div>

            <FormField
              control={profileForm.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nome</FormLabel>
                  <FormControl>
                    <Input placeholder="Seu nome" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <div>
              <FormLabel>E-mail</FormLabel>
              <Input value={profile?.email ?? ""} disabled className="mt-2 bg-muted" />
              <p className="mt-1 text-xs text-muted-foreground">O e-mail não pode ser alterado.</p>
            </div>

            <Button type="submit" disabled={savingProfile}>
              {savingProfile ? "Salvando..." : "Salvar perfil"}
            </Button>
          </form>
        </Form>
      </section>

      <section className="rounded-lg border border-border bg-card p-6 shadow-sm">
        <h2 className="mb-4 text-lg font-semibold">Alterar senha</h2>
        <Form {...passwordForm}>
          <form onSubmit={passwordForm.handleSubmit(handlePasswordSubmit)} className="space-y-4">
            <FormField
              control={passwordForm.control}
              name="currentPassword"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Senha atual</FormLabel>
                  <FormControl>
                    <Input type="password" autoComplete="current-password" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={passwordForm.control}
              name="newPassword"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nova senha</FormLabel>
                  <FormControl>
                    <Input type="password" autoComplete="new-password" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={passwordForm.control}
              name="confirmPassword"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Confirmar nova senha</FormLabel>
                  <FormControl>
                    <Input type="password" autoComplete="new-password" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <Button type="submit" disabled={savingPassword}>
              {savingPassword ? "Alterando..." : "Alterar senha"}
            </Button>
          </form>
        </Form>
      </section>
    </div>
  );
}
