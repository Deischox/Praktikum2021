var margin, width, height, svg, x, y;
initBarChart()

/**
 * Initilize the Barchart
 */
function initBarChart()
{
  // set the dimensions and margins of the graph
  margin = {top: 30, right: 30, bottom: 70, left: 60},
  width = 860 - margin.left - margin.right,
  height = 400 - margin.top - margin.bottom;

  // append the svg object to the body of the page
  svg = d3.select("#my_barchart")
  .append("svg")
  .attr("width", width + margin.left + margin.right)
  .attr("height", height + margin.top + margin.bottom)
  .append("g")
  .attr("transform",
        "translate(" + margin.left + "," + margin.top + ")");

  // X axis
  x = d3.scaleBand()
  .range([ 0, width ])
  .domain(0)

  .padding(0.2);
  svg.append("g")
  .attr("transform", "translate(0," + height + ")")
  .attr("class", "axisWhite")


  .attr("id", "xAxis")
  .attr("fill", "#69b3a2")
  .call(d3.axisBottom(x))






  // Add Y axis
  y = d3.scaleLinear()
  .domain([0, 0])
  .range([ height, 0]);
  svg.append("g")
  .attr("class", "myYaxis")
  .attr("class", "axisWhite")
  .call(d3.axisLeft(y));


}

 /**
  *  A function that create / update the plot for a given variable:
  * @param {List} data - List with the data
  * @param {int} max - Max Value on the Y-Scale
  */
 function update(data, max) {
 
  
   try{

     document.getElementById('yAxisBar').remove()
   }catch(e)
   {
     console.log(e)
   }
   
   var y = d3.scaleLinear()
   .domain([0, max])
   .range([ height, 0]);
   svg.append("g")
   .attr("class", "myYaxis")
   .attr("class", "axisWhite")
   .attr("id", "yAxisBar")
   .call(d3.axisLeft(y));

   var u = svg.selectAll("rect").data(data);
   document.getElementById("xAxis").remove();
   x = d3.scaleBand()
   .range([ 0, width ])
   .domain(data.map(function(d) { return d.group; }))
   
   .padding(0.2);
   svg.append("g")
   .attr("transform", "translate(0," + height + ")")
   .attr("class", "axisWhite")
   .attr("id", "xAxis")
   .attr("fill", "#69b3a2")
   .call(d3.axisBottom(x))

   u
     .enter()
     .append("rect")
     .merge(u)
     .transition()
     .duration(1000)
       .attr("x", function(d) { return  x(d.group); })
       .attr("y", function(d) { return y(d.value); })
       .attr("width", x.bandwidth())
       .attr("height", function(d) { return height - y(d.value); })
       .attr("fill", "#69b3a2")
 }

