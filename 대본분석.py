import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate('speechking-787ec-firebase-adminsdk-6mz9r-00885bb0e1.json')
firebase_admin.initialize_app(cred,{
    'databaseURL' : 'https://speechking-787ec-default-rtdb.firebaseio.com/'
})

# 데이터 변경 이벤트 콜백 함수
def handle_data_change(event):
    print('데이터 변경 이벤트 발생:', event.data)

    from konlpy.tag import Kkma, Okt
    from nltk.tokenize import sent_tokenize, word_tokenize
    from collections import defaultdict
    from firebase_admin import db

    okt = Okt()

    ref = db.reference('text')
    text = ref.get()

    sentences = sent_tokenize(text)

    with open('stopwords-ko.txt', 'r', encoding='utf-8') as f:
        stop_words = set(f.read().split())

    word_freq = defaultdict(int)
    for sentence in sentences:
        words = word_tokenize(sentence)
        for word in words:
            if word.lower() not in stop_words:
                word_freq[word.lower()] += 1

    max_freq = max(word_freq.values())

    for word in word_freq:
        word_freq[word] /= max_freq

    scores = defaultdict(int)
    for sentence in sentences:
        words = word_tokenize(sentence)
        for word in words:
            if word.lower() in word_freq:
                scores[sentence] += word_freq[word.lower()]

    top_sentence_ratio = 0.3
    top_sentence_count = int(len(sentences) * top_sentence_ratio)

    sentence_scores = defaultdict(int)
    for sentence in sentences:
        words = word_tokenize(sentence)
        for word in words:
            if word.lower() in word_freq:
                sentence_scores[sentence] += word_freq[word.lower()]

    top_sentences = sorted(sentence_scores.items(), key=lambda x: x[1], reverse=True)[:top_sentence_count]

    '''print("중요 문장 : ")'''
    total_sentence = ''
    for sentence, score in top_sentences:
        print(sentence)
        total_sentence += "<p>" + sentence + "</p>\n"

    result_str = ""
    keyword_list = list()

    for sentence, score in top_sentences:
        topic_sentences = []
        for s in sentences:
            if sentence in s:
                topic_sentences.append(s)
        topic_sentence = ' '.join(topic_sentences)
        tokens = okt.pos(topic_sentence)
        keywords = [token[0] for token in tokens if token[1] in ['Noun', 'Adjective'] and token[0] not in stop_words]
        unique_keywords = list(set(keywords))
        for keyword in unique_keywords:
            sentence = sentence.replace(keyword, f"<span style='color:red'>{keyword}</span>", 1)
            keyword_list.append(keyword)
            total_sentence = total_sentence.replace(keyword, f"<span style='color:red'>{keyword}</span>", 1)
            text = text.replace(keyword, f"<span style='color:red'>{keyword}</span>", 1)
        result_str += f"<p>{sentence}</p>\n"

    keyword_list = set(keyword_list)

    for keyword in keyword_list:
        total_sentence = total_sentence.replace(keyword, f"<span style='color:red'>{keyword}</span>", 1)
        text = text.replace(keyword, f"<span style='color:red'>{keyword}</span>", 1)

    '''print("키워드 강조 문장 : ")
    print(total_sentence)
    print()'''

    print("전체 문장 :")
    print(text)

    text_reference = db.reference('output')
    text_reference.set(text)

    keyword_reference = db.reference('keyword_list')
    keyword_reference.set(list(keyword_list))

# 데이터 변경 이벤트를 감지할 Firebase Realtime Database 경로 지정
ref = db.reference('/text')

# 데이터 변경 이벤트 콜백 등록
ref.listen(handle_data_change)

# 프로그램이 종료되지 않도록 유지
while True:
    pass


