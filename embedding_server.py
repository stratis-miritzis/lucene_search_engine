from flask import Flask, request
from gensim.scripts.glove2word2vec import glove2word2vec
from gensim.models import KeyedVectors

import gensim.downloader as api
import gensim 
import gensim.models
from gensim.models import Word2Vec
from gensim import utils
import pandas as pd

app = Flask(__name__)

@app.route('/simular', methods=['POST'])
def simular():
    global model
    global selection

    data = request.get_json()
    string = data.get('string', '')
    simular = string.lower()

    words = simular.split(" ")

    print(string)
    ret = " OR "

    for i in words: 
        try:
            if selection == "1":
                simularitie1 = model.most_similar(i)[0]
                simularitie2 = model.most_similar(i)[1]
                for j in model.most_similar(i):
                    print(j)
            else:
                for o in range(0,6):
                    simularitie = model.wv.most_similar(i)[o]
                    ret += str(simularitie[0]) + "^" + str(round(simularitie[1], 3)) + " "
        except:
            print("Key not present in vocabulary")

    print(ret)

    return ret

if __name__ == '__main__':
    global model
    global selection
# Google pretrained word2vec model
# CBOW embedding
# SkipGram embedding
    print("Choose embedding model")
    print("1: Google pretrained word2vec model")
    print("2: CBOW embedding model")
    print("3: SkipGram embedding model")

    selection = input()
    if selection == "1":
        print("Using Google pretrained word2vec model\n")
        path = api.load("word2vec-google-news-300", return_path=True)
        model = gensim.models.KeyedVectors.load_word2vec_format(path, binary=True)
        print(model.most_similar('metal'))
    elif selection == "2":
        print("Training CBOW embedding model\n")
        df = pd.read_csv('reviews.csv', encoding='utf-8',sep='~')
        df = df.dropna(subset=['content'])
        train_gsim = [utils.simple_preprocess(x) for x in df['content']]
        model = gensim.models.Word2Vec(train_gsim, min_count = 1, window = 10)
        print(model.wv.most_similar('metal'))
    elif selection == "3":
        print("Training SkipGram embedding model\n")
        df = pd.read_csv('reviews.csv', encoding='utf-8',sep='~')
        df = df.dropna(subset=['content'])
        train_gsim = [utils.simple_preprocess(x) for x in df['content']]
        model = gensim.models.Word2Vec(train_gsim, min_count = 1, window = 10, sg = 1)
        print(model.wv.most_similar('metal'))
        



    app.run()
