import numpy as np
import cv2
from tensorflow import keras
from tensorflow.keras.preprocessing.image import ImageDataGenerator
import os
import json
import time
import base64

def save_to_file(json_data, label):
    file_name= './label_data/'+ str(int(time.time())) +".json"
    file_name2='./label_data/'+ str(int(time.time())) +".txt"
    prediction_actual = label+":"+" "
    with open(file_name, 'w') as outfile:
        json.dump(data, outfile)
    with open(file_name2, 'w') as outfile:
        outfile.write(prediction_actual)

def save_to_file_1(textdata):
    file_name= './input/'+ str(int(time.time())) +".txt"
    print("saving to file", file_name)
    with open(file_name, 'w') as outfile:
        print("writing to file", file_name)
        outfile.write(textdata)
    

if __name__ == "__main__":

    print("loading model")
    model = keras.models.load_model('./tfformat')
    print("model loaded")

    print("display mode")
    #model.load_weights('model.h5')
    print("loaded weights")
    # prevents openCL usage and unnecessary logging messages
    cv2.ocl.setUseOpenCL(False)
    #keras.models.save_model(model, "tfformat")

    # dictionary which assigns each label an emotion (alphabetical order)
    emotion_dict = {0: "angry", 1: "disgusted", 2: "fearful", 3: "happy", 4: "neutral", 5: "sad", 6: "Surprised"}

    # start the webcam feed
    print("Start the video")
    cap = cv2.VideoCapture(0)
    while True:
        # Find haar cascade to draw bounding box around face
        ret, frame = cap.read()
        if not ret:
            break
        # 
        facecasc = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        faces = facecasc.detectMultiScale(gray,scaleFactor=1.3, minNeighbors=5)

        for (x, y, w, h) in faces:
            cv2.rectangle(frame, (x, y-50), (x+w, y+h+10), (255, 0, 0), 2)
            roi_gray = gray[y:y + h, x:x + w]
            cropped_img = np.expand_dims(np.expand_dims(cv2.resize(roi_gray, (48, 48)), -1), 0)
            #print(cropped_img)
            data={"array": cropped_img.tolist()}
            s = base64.b64encode(cropped_img)
            
            print("saving to file", cropped_img.shape)
            # save_to_file_1(s.decode('utf-8'))
	        #save_to_file_1(s.decode('utf-8'))
            prediction = model.predict(cropped_img)
            maxindex = int(np.argmax(prediction))
            save_to_file(data, emotion_dict[maxindex])
            cv2.putText(frame, emotion_dict[maxindex], (x+20, y-60), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2, cv2.LINE_AA)

        cv2.imshow('Video', cv2.resize(frame,(1600,960),interpolation = cv2.INTER_CUBIC))
        time.sleep(1)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()
