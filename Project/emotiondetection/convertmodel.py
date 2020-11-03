from tensorflow import keras
model = keras.models.load_model('model.h5')
keras.experimental.export_saved_model(model, 'proto')
