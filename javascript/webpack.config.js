const path = require('path');

// Use for production.
const distFolder = path.resolve(__dirname, '../templates/webhelp-documentation-template/js');
// Uncomment to use for development.
// const distFolder = path.resolve(__dirname, '../doc/project-documentation/out/webhelp-responsive/oxygen-webhelp/template/js');

module.exports = {
	entry: { //location of your main js file
		algoliaEs6: './app/main.js'
	},
    output: {
        path: distFolder,
        filename: '[name].js',  // where js files would be bundled to
    },
    module: {
	    rules: [
	        {
	            test: /\.js$/, //using regex to tell babel exactly what files to transcompile
	            exclude: /node_modules/, // files to be ignored
	            use: {
	                loader: 'babel-loader' // specify the loader
	            } 
	        },
			{
				test: /\.css$/,
				use: [
					'style-loader',
					'css-loader'
				]
			}
		]
	}
 } 