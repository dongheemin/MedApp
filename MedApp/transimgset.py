import numpy as np

# 랜덤시드 고정시키기
np.random.seed(20190709)

from keras.preprocessing.image import ImageDataGenerator, array_to_img, img_to_array, load_img

# 데이터셋 불러오기
data_aug_gen = ImageDataGenerator(rescale=1./255,
                                  rotation_range=90,
                                  #width_shift_range=0.5,
                                  #height_shift_range=0.5,
                                  shear_range=0.5,
                                  #zoom_range=[1.0, 1.2],
                                  horizontal_flip=True,
                                  vertical_flip=True,
                                  fill_mode='nearest')

nameset=['Anacsen', 'Bearoze', 'BenfoBel', 'Bita1000', 'Cleardin', 'Cospen', 'Dexpeed', 'Diarofen', 'Dyjest', 'EpinS', 'EZN6 ANY', 'EZN6 EVE', 'Famecone', 'Gnalen', 'GreenCall', 'HavenU', 'Hetomasin', 'Hipen', 'Iveda', 'Jungrohwan', 'Krabutin', 'MayQueen', 'Naprocsen', 'OmecopeS', 'Penssac', 'Pieon', 'Qzime', 'Ropeberon', 'Socncool', 'Tanasen']

for name in nameset:
    print(name)
    for j in range(1,17):
        fname = "{}.jpg".format("{0:05d}".format(j))
        img = load_img('./img/train/'+name+'/'+fname)
        x = img_to_array(img)
        x = x.reshape((1,) + x.shape)
        i = 0
        # 이 for는 무한으로 반복되기 때문에 우리가 원하는 반복횟수를 지정하여, 지정된 반복횟수가 되면 빠져나오도록 해야합니다.
        for batch in data_aug_gen.flow(x, batch_size=1, save_to_dir='./img/train/'+name, save_prefix='Boom', save_format='jpg'):
            i += 1
            if i > 30:
                break
