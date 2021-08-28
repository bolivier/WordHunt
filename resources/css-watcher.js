// include depenencies (a-z)

const fs = require("fs");
const mkdirp = require("mkdirp");
const postcss = require("postcss");

// configure input, output and processors:

const inputFilename = "resources/css/styles.css";
const outputDir = "resources/public/static/css/";
const outputFilename = "resources/public/static/css/styles.css";

const processors = [
  require("postcss-import"), // combine imports into one file
  require("postcss-custom-properties"), // replace variables by their calculated values
  require("postcss-color-function"), // replaces color functions with rgba values
  require("postcss-color-rgba-fallback"), // adds a hex value before every rgba value
  require("pixrem"), // adds pixel fallback before every rem value
  require("postcss-calc"), // pre-calculcates calc functions where possible
  require("autoprefixer"), // vendor prefix for older browsers
];

if (process.env.NODE_ENV === "production") {
  processors.push(require("cssnano")); // minify css
}

const options = {
  from: "styles.css",
  to: "styles.css",
};

console.log("options: ", options);

// process input and write output to disk:
postcss(processors)
  .process(fs.readFileSync(inputFilename), options)
  .then(writeOutput)
  .catch((err) => handleError(err.message));

function writeOutput(result) {
  mkdirp(outputDir, {})
    .then(() => {
      fs.writeFile(outputFilename, result.css, handleError);
      if (result.map) {
        fs.writeFile(`${outputFilename}.map`, result.map, handleError);
      }
    })
    .catch(handleError);
}

function handleError(err) {
  if (err) {
    console.log("an error occurred");
    console.log(err.stack);
    console.log(err);
    process.exit(1);
  }
}
