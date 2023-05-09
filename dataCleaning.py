import pandas as pd
import sqlite3

# Read sqlite query results into a pandas DataFrame
con = sqlite3.connect("database.sqlite")
content = pd.read_sql_query("SELECT * from content", con)
labels = pd.read_sql_query("SELECT * from labels", con)
reviews = pd.read_sql_query("SELECT * from reviews", con)

final = content.merge(reviews, left_on='reviewid', right_on='reviewid')
final = final.merge(labels, left_on='reviewid', right_on='reviewid')
final = final.fillna('_')

final['content'] = final['content'].str.replace('~','',regex=True)
final['url'] = final['url'].str.replace('~','',regex=True)
final['label'] = final['label'].str.replace("~",'',regex=True)
final['title'] = final['title'].str.replace('~','',regex=True)
final['author'] = final['author'].str.replace('~','',regex=True)
final['author_type'] = final['author_type'].str.replace('~','',regex=True)
print(final.isnull().sum())


final['content'] = final['content'].str.replace('\v',' ',regex=True)
final['content'] = final['content'].str.replace('\\',' ',regex=True)
final['content'] = final['content'].str.replace("\r",' ',regex=True)
final['content'] = final['content'].str.replace("\n",' ',regex=True)
final['content'] = final['content'].str.replace("\t",'',regex=True)
final['content'] = final['content'].str.replace("\"",'',regex=True)
final['content'] = final['content'].str.replace("      ",'',regex=True)







final.head(20).to_csv('20_reviews.csv',index=False, encoding='utf-8',sep='~',na_rep='_')
final.to_csv('reviews.csv',index=False, encoding='utf-8',sep='~',na_rep='_')

con.close()

print(final)
