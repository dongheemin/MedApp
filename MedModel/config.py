import tensorflow as tf

FLAGS = tf.app.flags.FLAGS

tf.app.flags.DEFINE_integer('raw_height', 64, '')
tf.app.flags.DEFINE_integer('raw_width', 64, '')
tf.app.flags.DEFINE_integer('height', 64, '')
tf.app.flags.DEFINE_integer('width', 64, '')
tf.app.flags.DEFINE_integer('depth', 3, '')
tf.app.flags.DEFINE_integer('num_class', 2, '')
tf.app.flags.DEFINE_integer('num_conv', 2, '')
tf.app.flags.DEFINE_integer('kernel_size', 19, '')
tf.app.flags.DEFINE_integer('pool_size', 2, '')
tf.app.flags.DEFINE_integer('num_map', 32, '')
tf.app.flags.DEFINE_integer('num_fc_layer', 2, '')
tf.app.flags.DEFINE_integer('num_fc_input', 1024, '')
tf.app.flags.DEFINE_integer('max_steps', 1000, '')
tf.app.flags.DEFINE_integer('batch_size', 10, '')
tf.app.flags.DEFINE_float('learning_rate', 0.0001, '')

tf.app.flags.DEFINE_string('data_dir', './data', '')               #이미지 데이터 위치
tf.app.flags.DEFINE_string('checkpoint_dir', './checkpoint', '')
