CREATE TABLE IF NOT EXISTS public.products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price_cents INTEGER NOT NULL CHECK (price_cents >= 0),
    ingredients TEXT,
    story TEXT,
    emoji VARCHAR(16),
    slug VARCHAR(160) UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Trigger de atualização automática de updated_at
CREATE OR REPLACE FUNCTION public.set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_products_updated_at ON public.products;

CREATE TRIGGER trg_products_updated_at
BEFORE UPDATE ON public.products
FOR EACH ROW
EXECUTE FUNCTION public.set_updated_at();

-- Índices para melhorar performance
CREATE INDEX IF NOT EXISTS idx_products_is_active ON public.products(is_active);
CREATE INDEX IF NOT EXISTS idx_products_slug ON public.products(slug);

-- Dados iniciais (seed)
INSERT INTO public.products (name, description, price_cents, ingredients, story, emoji, slug)
VALUES
('Panna Cotta Clássica', 'Sobremesa italiana cremosa', 2500, 'Creme de leite, açúcar, baunilha', 'Inspirada na tradição piemontesa', '🍮', 'panna-cotta-classica'),
('Pão de Mel Especial', 'Cobertura de chocolate belga', 1800, 'Mel, especiarias, chocolate', 'Receita artesanal refinada', '🍯', 'pao-de-mel-especial')
ON CONFLICT (slug) DO NOTHING;
