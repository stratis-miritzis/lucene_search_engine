import pandas as pd
import sqlite3

# Read sqlite query results into a pandas DataFrame
con = sqlite3.connect("database.sqlite")
content = pd.read_sql_query("SELECT * from content", con)
labels = pd.read_sql_query("SELECT * from labels", con)
reviews = pd.read_sql_query("SELECT * from reviews", con)

final = content.merge(reviews, left_on='reviewid', right_on='reviewid')
final = final.merge(labels, left_on='reviewid', right_on='reviewid')

final.head(20).to_csv('20_reviews.csv',index=False, encoding='utf-8')
final.to_csv('reviews.csv',index=False, encoding='utf-8')

con.close()