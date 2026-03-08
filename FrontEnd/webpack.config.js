const path = require('path');

module.exports = {
  mode: 'development',
  entry: {

    

    index: "./src/codigos_tsc/codigos_doom/index.ts",
    Cadastro_Empresas: "./src/codigos_tsc/codigos_doom/Cadastro_empresa.ts",
    Cadastro_Candidatos: "./src/codigos_tsc/codigos_doom/Cadastro_candidato.ts",
    Menu_empresa: "./src/codigos_tsc/codigos_doom/Menu_empresa.ts",
    Menu_candidato: "./src/codigos_tsc/codigos_doom/Menu_candidato.ts",
    Visualizacao_vagas_candidato: "./src/codigos_tsc/codigos_doom/visualizacao_vagas_candidatos.ts",
    Visualizacao_vagas_empresa: "./src/codigos_tsc/codigos_doom/visualizacao_vaga_empresa.ts",
    Edicao_candidato: "./src/codigos_tsc/codigos_doom/edicao_candidato.ts",
    Edicao_empresa: "./src/codigos_tsc/codigos_doom/edicao_empresa.ts",


  






    
  },

  module: {
    rules: [
      {
        test: /\.ts$/,
        use: 'ts-loader',
        exclude: /node_modules/,
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader'], 
      },
    ],
  },

  resolve: {
    extensions: ['.ts', '.js'],  
  },

  output: {
    filename: '[name].bundle.js',
    path: path.resolve(__dirname, 'dist'),
    clean: true, 
  },

  
};