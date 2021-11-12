import mysql.connector as mysql
from random import randint, sample, uniform

connection = mysql.connect(
    host='localhost',
    user='root',
    password='123456',
    database='demo_module_3'
)
cursor = connection.cursor()

def clear_table(cursor, connection, table_name):
    clear_table_query = f'TRUNCATE TABLE {table_name}'
    cursor.execute(clear_table_query)
    connection.commit()

# Clear tables
# clear_table(cursor, connection, '`order`')
# clear_table(cursor, connection, 'tag_certificate')
# clear_table(cursor, connection, 'gift_certificate')
# clear_table(cursor, connection, '`user`')
# clear_table(cursor, connection, 'tag')


# Fill "Gift_certificate" table
# add_certificate_query = 'INSERT INTO Gift_certificate (name, description, price, duration, create_date, last_update_date, is_available) VALUES (%s, %s, %s, %s, DEFAULT, DEFAULT, true)'
#
# certificate_values = [(f'Certificate {i}', f'Description {i}', randint(100, 500), randint(1, 31)) for i in range(10000)]
#
# cursor.executemany(add_certificate_query, certificate_values)
# connection.commit()
#
# # Fill "Tag" table
# add_tags_query = 'INSERT INTO tag (name) VALUES (%s)'
#
# tag_values = [((f'Tag {i}')) for i in range(1000)]
# cursor.executemany(add_tags_query, tag_values)
# connection.commit()
#
# Add m-2-m relations for Certificates and Tags
get_certificates_query = "SELECT * FROM gift_certificate"
get_tags_query = "SELECT * FROM tag"
add_certificate_has_tag_query = "INSERT INTO tag_certificate (certificate_id, tag_id) VALUES (%s, %s)"

# Fetch all certificates
cursor.execute(get_certificates_query)
certificates = cursor.fetchall()

# Fetch all tags
# cursor.execute(get_tags_query)
# tags = cursor.fetchall()
#
# # Generate and add relations
# for certificate in certificates:
#     related_tags = sample(tags, randint(1, 5))
#     for tag in related_tags:
#         cursor.execute(add_certificate_has_tag_query, (certificate[0], tag[0]))
#     connection.commit()

# Fill "User" table
# add_users_query = "INSERT INTO `user` (username, balance) VALUES (%s, %s)"
# users_data = [(f'username {i}', round(uniform(1000, 200000), 2)) for i in range(1000)]
# cursor.executemany(add_users_query, users_data)
# connection.commit()

# Fill "order" table
add_orders_query = "INSERT INTO `order` (total_price, buy_date, user_id, certificate_id) VALUES (%s, DEFAULT, %s, %s)"
orders_data = []
for i in range(0, 100000):
    user_id = randint(1, 1000)
    certificate_id = randint(1, 10000)
    orders_data.append([certificates[certificate_id][3], user_id, certificate_id])
cursor.executemany(add_orders_query, orders_data)
connection.commit()

connection.close()
