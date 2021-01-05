'''
Copyright (c) 2018-present Atul Balaji

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
'''

from ray import serve

import os
import tempfile
import numpy as np
import ray
import json


class EmotionModel:
    def __init__(self, model_path):
        import tensorflow as tf
        from tensorflow import keras
        self.model_path = model_path
        self.model = keras.models.load_model(model_path)

    def __call__(self, flask_request):
         #label to emotions
        emotion_dict = {0: "angry", 1: "disgusted", 2: "fearful", 3: "happy", 4: "neutral", 5: "sad", 6: "surprised"}
        #in_data = json.loads(flask_request.data.decode('ascii'))
        in_data = json.loads(flask_request.args['np'])
        # Step 1: transform HTTP request -> tensorflow input
        input_array = np.array(in_data["array"])
        prediction = self.model(input_array)
        maxindex = int(np.argmax(prediction))

        # Step 3: tensorflow output -> web output
        return str(emotion_dict[maxindex])

model_path = './tfformat'
ray.init(address="auto")
client = serve.start(http_host="0.0.0.0",detached=True)
client.create_backend("em:v1", EmotionModel, model_path)
client.create_endpoint("emotion_classifier", backend="em:v1", route="/emotions")

