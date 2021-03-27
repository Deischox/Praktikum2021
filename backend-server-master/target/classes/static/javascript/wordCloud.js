/**
 * Function to get the Values of the Table and then change the wordcloud acording to the Values in the Table
 */
function wordCloud()
{
  try
  {
    var old = document.getElementById('my_dataviz').children;
    console.log(old)
    for(var i = 0; i < old.length; i++)
    {
      old[i].remove()
    }
    }catch(e)
    {
      console.log(e); 
    }



var w = document.getElementsByClassName('data-words');
var per = document.getElementsByClassName('data-percentage');
var arr = Array.prototype.slice.call( w, 0 );
var index;
var words = [];
var values = [];
for (index = 0; index < w.length; ++index) {
    words.push(w[index].innerHTML);
    values.push(parseFloat(per[index].innerHTML.split("%")[0]))
}



maxValue = Math.max.apply(Math, values);
var fontSizeMultiply = (1/maxValue)*80;


//List of Words
var myWords = words;

// set the dimensions and margins of the graph
var margin = {top: 10, right: 10, bottom: 10, left: 10},
    width = 850 - margin.left - margin.right,
    height = 850 - margin.top - margin.bottom;

// append the svg object to the body of the page
var svg = d3.select("#my_dataviz").append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform",
          "translate(" + margin.left + "," + margin.top + ")");

// Constructs a new cloud layout instance. It run an algorithm to find the position of words that suits your requirements
var layout = d3.layout.cloud()
  .size([width, height])
  .words(myWords.map(function(d) {
      return {text: d, size: 10};
    }))
    .padding(1)
    .font("Impact")
    .fontSize(function(d) { return Math.max(per[words.indexOf(d.text)].innerHTML.split("%")[0]*fontSizeMultiply,10)})
    .on("end", draw);
layout.start();


// This function takes the output of 'layout' above and draw the words
// Better not to touch it. To change parameters, play with the 'layout' variable above
function draw(words) {
  svg
    .append("g")
      .attr("transform", "translate(" + layout.size()[0] / 2 + "," + layout.size()[1] / 2 + ")")
      .selectAll("text")
        .data(words)
      .enter().append("text")
        .style("font-size", function(d) { return d.size + "px"; })
        .style("fill", "#69b3a2")
        .attr("text-anchor", "middle")
        .attr("transform", function(d) {
          return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
        })
        .text(function(d) { return d.text; });
}
}