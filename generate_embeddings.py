import os
import psycopg2
from sentence_transformers import SentenceTransformer


DATABASE_URL = "postgresql://postgres.zpwdhvlxdibzarahtlxf:KobeIsMyHomie@aws-1-ap-south-1.pooler.supabase.com:5432/postgres"


# Local, open-source model: no paid AI API required.
model = SentenceTransformer("sentence-transformers/all-MiniLM-L6-v2")

connection = psycopg2.connect(DATABASE_URL)

try:
    with connection.cursor() as cursor:
        cursor.execute("""
            SELECT
                p.id,
                p.name,
                p.description,
                p.price,
                c.name AS category_name
            FROM public.products p
            JOIN public.categories c
                ON c.id = p.category_id
            ORDER BY p.id
        """)

        products = cursor.fetchall()

        print(f"Found {len(products)} products.")

        for product_id, name, description, price, category_name in products:
            text_to_embed = (
                f"Product: {name}. "
                f"Category: {category_name}. "
                f"Description: {description or ''}. "
                f"Price: {price}."
            )

            embedding = model.encode(
                text_to_embed,
                normalize_embeddings=True
            ).tolist()

            if len(embedding) != 384:
                raise ValueError(
                    f"Unexpected embedding size for product {product_id}"
                )

            cursor.execute("""
                INSERT INTO public.product_embeddings
                    (product_id, embedding, updated_at)
                VALUES
                    (%s, %s::extensions.vector, NOW())
                ON CONFLICT (product_id)
                DO UPDATE SET
                    embedding = EXCLUDED.embedding,
                    updated_at = NOW()
            """, (product_id, str(embedding)))

            print(f"Saved embedding for product {product_id}: {name}")

    connection.commit()
    print("All product embeddings saved successfully.")

except Exception:
    connection.rollback()
    raise

finally:
    connection.close()
