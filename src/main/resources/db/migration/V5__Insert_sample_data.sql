INSERT INTO franchises (id, name) VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 'McDonald''s'),
    ('550e8400-e29b-41d4-a716-446655440002', 'Subway'),
    ('550e8400-e29b-41d4-a716-446655440003', 'KFC');

INSERT INTO branches (id, name, franchise_id) VALUES
    ('550e8400-e29b-41d4-a716-446655440011', 'McDonald''s Centro', '550e8400-e29b-41d4-a716-446655440001'),
    ('550e8400-e29b-41d4-a716-446655440012', 'McDonald''s Norte', '550e8400-e29b-41d4-a716-446655440001'),
    ('550e8400-e29b-41d4-a716-446655440021', 'Subway Plaza', '550e8400-e29b-41d4-a716-446655440002'),
    ('550e8400-e29b-41d4-a716-446655440031', 'KFC Mall', '550e8400-e29b-41d4-a716-446655440003');

INSERT INTO products (name, stock, branch_id) VALUES
    ('Big Mac', 50, '550e8400-e29b-41d4-a716-446655440011'),
    ('Quarter Pounder', 30, '550e8400-e29b-41d4-a716-446655440011'),
    ('McNuggets', 100, '550e8400-e29b-41d4-a716-446655440011'),
    ('Big Mac', 25, '550e8400-e29b-41d4-a716-446655440012'),
    ('McFlurry', 75, '550e8400-e29b-41d4-a716-446655440012'),
    ('Italian BMT', 40, '550e8400-e29b-41d4-a716-446655440021'),
    ('Turkey Breast', 35, '550e8400-e29b-41d4-a716-446655440021'),
    ('Original Recipe', 60, '550e8400-e29b-41d4-a716-446655440031'),
    ('Hot Wings', 45, '550e8400-e29b-41d4-a716-446655440031');