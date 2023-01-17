const path = require('path');

module.exports = {
  mode:'development',
  entry: {
    index: path.resolve(__dirname, 'src/main/resources/static/index.jsx'),
  },
  output: {
    path: path.resolve(__dirname, 'src/main/resources/static/bundles/'),
    filename: '[name].jsx'
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-react']
          }
        }
      }
    ]
  }
};
