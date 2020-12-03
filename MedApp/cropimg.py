from PIL import Image
import numpy as np

# 랜덤시드 고정시키기
np.random.seed(20190709)

nameset=['Anacsen', 'Bearoze', 'BenfoBel', 'Bita1000', 'Cleardin', 'Cospen', 'Dexpeed', 'Diarofen', 'Dyjest', 'EpinS', 'EZN6 ANY', 'EZN6 EVE', 'Famecone', 'Gnalen', 'GreenCall', 'HavenU', 'Hetomasin', 'Hipen', 'Iveda', 'Jungrohwan', 'Krabutin', 'MayQueen', 'Naprocsen', 'OmecopeS', 'Penssac', 'Pieon', 'Qzime', 'Ropeberon', 'Socncool', 'Tanasen']
#area = (16,16,240,240)

for name in nameset:
    print(name)
    for j in range(1,5):
        img = Image.open('./realimg/train/'+name+'/'+name+' ('+str(j)+').jpg')
        cropped_img=img.crop(area)
        fname = "{}.jpg".format("{0:05d}".format(j))
        savename = './img/train/'+name+'/'+fname
        #img.save(savename)
	cropped_img.save(savename)
        print('save file '+savename+'...')





