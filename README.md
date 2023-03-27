# anaktisi

**Φάση 1: Αρχικός σχεδιασμός και συλλογή δεδομένων**

**Ζήτημα 1(α)**

Για την εργασία του μαθήματος, επιλέξαμε να χρησιμοποιήουμε δεδομένα της διαδικτυακής πλατφόρμας pitchfork. To pitchfork είναι ένα music-focused site στο οποίο ο καθένας μπορεί να βρει νέα σχετικά με την μουσική, κριτικές άλμπουμ/τραγουδιών, συνεντεύξεις με μουσικούς καθώς και άρθρα για την μουσική κουλτούρα και τις τάσεις. Γιατί λοιπόν επιλέξαμε το pitchfork? Η επιλογή έγινε διότι το σάιτ του pitchfork έχει μια εκτενή αρχειοθήκη κριτικών και άρθρων που χρονολογούνται από την ίδρυσή του, καθιστώντας το μια πολύτιμη πηγή πληροφοριών για τους φαν της μουσικής.

Σχετικά με την συλλογή των εγγράφων που μας ζητάται, το αρχικό database από το οποίο πήραμε τα δεδομένα μας είναι ένα dataset του pithfork με συνολικά 18.393 reviews (σε μορφή sqlite database) το οποίο παραθέτουμε παρακάτω:
https://www.kaggle.com/datasets/nolanbconaway/pitchfork-data

Όπως παρατηρούμε το database αυτό περιέχει πληροφορίες σχετικά με άλμπουμ/τραγούδια μουσικών καλλιτεχνών. Οι πίνακες (tables) οι οποίοι έχουμε είναι οι εξής:

artists (reviewId, artist)

content (reviewId, content)

genres (reviewId, genre)

labels (reviewId, label)

reviews (reviewId, title, artist, url, score, best_new_music, author, author_type, pub_date, pub_weekday, pub_day, pub_month, pub_year)

years (reviewId, year)

Πήραμε λοιπόν αυτά τα δεδομένα και με κάποιες τροποποιήσεις (SQL2CSV.ipynb στο repository) φτιάξαμε ένα ενιαίο αρχείο csv που περιέχει όλα τα δεδομένα που θέλουμε να χρησιμοποιήσουμε.

Το τελικό αρχείο που φτιάξαμε και από το οποίο θα αντλούμε κάθε πληροφορία έχει ως fields τα εξής:

reviews(reviewId, content, title, artist, url, score, best_new_music, author, author_type, pub_date, pub_weekday, pub_day, pub_month, pub_year, label)
