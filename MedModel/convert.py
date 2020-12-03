import os
import tensorflow as tf
import random
import matplotlib
#matplotlib.use('Qt5Agg')
import matplotlib.pyplot as plt
import numpy as np
import config

FLAGS = tf.app.flags.FLAGS

def get_filename_set(data_set):
    labels = []
    filename_set = []

    with open(FLAGS.data_dir + '/labels.txt') as f:
        for line in f:
            inner_list = [elt.strip() for elt in line.split(',')]
            labels += inner_list

    for i, lable in enumerate(labels): #Label 폴더 안에 파일들을 찾고 리스트에 입력
        list = os.listdir(FLAGS.data_dir  + '/' + data_set + '/' + lable)
        for filename in list:
            filename_set.append([i, FLAGS.data_dir  + '/' + data_set + '/' + lable + '/' + filename])

    random.shuffle(filename_set) # 파일이름과 라벨에 해당하는 번호를 묶어서 추가하고 Random하게 섞어줌
    return filename_set

#image 파일 Decode 및 Resize / 파일을 8bit 이미지로 casting
def read_jpeg(filename):
    value = tf.read_file(filename)
    decoded_image = tf.image.decode_jpeg(value, channels=FLAGS.depth)
    resized_image = tf.image.resize_images(decoded_image, [FLAGS.raw_height, FLAGS.raw_width])
    resized_image = tf.cast(resized_image, tf.uint8)

    return resized_image

def convert_images(sess, data_set):
    filename_set = get_filename_set(data_set)

    with open('./data/' + data_set + '_data.bin', 'wb') as f: #.bin 파일 생성
        for i in range(0, len(filename_set)):
            resized_image = read_jpeg(filename_set[i][1])
                                                              #파일이름과 라벨이 있는 filename_set을 read_jpeg를 통해 변환
            try:
                image = sess.run(resized_image)
            except Exception as e:
                print(e.message)
                continue

            #plt.imshow(np.reshape(image.data, [FLAGS.raw_height, FLAGS.raw_width, FLAGS.depth]))
            #plt.show()

            print(i, filename_set[i][0], image.shape)
            f.write(chr(filename_set[i][0]).encode())
            f.write(image.data)

#이미지 파일 로드
def read_raw_images(sess, data_set):
    filename = ['./data/' + data_set + '_data.bin']
    filename_queue = tf.train.string_input_producer(filename)

    #config의 크기로 record byte의 크기 설정
    record_bytes = (FLAGS.raw_height) * (FLAGS.raw_width) * FLAGS.depth + 1
    reader = tf.FixedLengthRecordReader(record_bytes=record_bytes)
    key, value = reader.read(filename_queue)
    record_bytes = tf.decode_raw(value, tf.uint8)

    tf.train.start_queue_runners(sess=sess)

    for i in range(0, 100):
        result = sess.run(record_bytes)
        print(i, result[0])
        image = result[1:len(result)]

        #plt.imshow(np.reshape(image, [FLAGS.raw_height, FLAGS.raw_width, FLAGS.depth]))
        #plt.show()

def main(argv = None):
    with tf.Session() as sess:
        convert_images(sess, 'train')
        convert_images(sess, 'eval')
        #read_raw_images(sess, 'eval')

if __name__ == '__main__':
    tf.app.run()
