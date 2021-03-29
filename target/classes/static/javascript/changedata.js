/**
 * Changes the Values in the Table and the Timeline 
 * @param {Array or List of word} array 
 */
function changeTable(array)
{
 
  var newdata = [];
  var values = [];
  var children = document.getElementById("basecamp").children;

  document.getElementById("loader").classList.toggle("d-none")

  if(array.words.length > 15)
  {
    for(i = 0; i < 16; i++)
      {
        children[i].children[1].innerHTML = array.words[i].word;
        children[i].children[2].innerHTML = array.words[i].percentage;
        values[i] = (parseFloat(array.words[i].percentage));
        group = array.words[i].word;
        value = array.words[i].percentage;

        newdata[i] = {group, value};
      }
  maxValue = Math.max.apply(Math,values);
  wordCloud();
  update(newdata, maxValue);

  }else
  {
    alert("This Hashtag hasn't enough data to visualize. Please select another Hashtag");
  }

  var ctx = document.getElementById('myChart').getContext('2d');

  

  ctx.innerHTML = "";
  var times = []
  var counter = []

  var map = new Map();
  for(i = 0; i < array.timeline.length; i++)
  {
    switch(array.timeline[i].word.substring(0,3))
    {
      case "Jan":
        map.set("01" + array.timeline[i].word.substring(3,9),array.timeline[i].percentage);
        break;
      case "Feb":
        map.set("02" + array.timeline[i].word.substring(3,9),array.timeline[i].percentage);
        break;
      case "Mar":
        map.set("03" + array.timeline[i].word.substring(3,9),array.timeline[i].percentage);
        break;
      case "Apr":
        map.set("04" + array.timeline[i].word.substring(3,9),array.timeline[i].percentage);
        break;
      case "May":
        map.set("05" + array.timeline[i].word.substring(3,9),array.timeline[i].percentage);
        break;
      case "Jun":
        map.set("06" + array.timeline[i].word.substring(3,9),array.timeline[i].percentage);
        break;
      case "Jul":
        map.set("07" + array.timeline[i].word.substring(3,9),array.timeline[i].percentage);
        break;
      case "Aug":
        map.set("08" + array.timeline[i].word.substring(3,9),array.timeline[i].percentage);
        break;
      case "Sep":
        map.set("09" + array.timeline[i].word.substring(3,9),array.timeline[i].percentage);
        break;
      case "Oct":
        map.set("10" + array.timeline[i].word.substring(3,9),array.timeline[i].percentage);
        break;
      case "Nov":
        map.set("11" + array.timeline[i].word.substring(3,9),array.timeline[i].percentage);
        break;
      case "Dez":
        map.set("12" + array.timeline[i].word.substring(3,9),array.timeline[i].percentage);
        break;
    }
    
    counter[i] = array.timeline[i].percentage;
  }
  const mapSort1 = new Map([...map.entries()].sort((a, b) => a[0] - b[0]));

  var x = 0;
  
  function setTimesAndValues(value, key, map)
  {
      switch(key.substring(0,2))
    {
      case "01":
        times[x] = "Jan " + key.substring(2,4);
        break;
      case "02":
        times[x] = "Feb " + key.substring(2,4);
        break;
      case "03":
        times[x] = "Mar " + key.substring(2,4);
        break;
      case "04":
        times[x] = "Apr " + key.substring(2,4);
        break;
      case "05":
        times[x] = "May " + key.substring(2,4);
        break;
      case "06":
        times[x] = "Jun " + key.substring(2,4);
        break;
      case "07":
        times[x] = "Jul " + key.substring(2,4);
        break;
      case "08":
        times[x] = "Aug " + key.substring(2,4);
        break;
      case "09":
        times[x] = "Sep " + key.substring(2,4);
        break;
      case "10":
        times[x] = "Oct " + key.substring(2,4);
        break;
      case "11":
        times[x] = "Nov " + key.substring(2,4);
        break;
      case "12":
        times[x] = "Dec " + key.substring(2,4);
        break;

      }
    
    
    counter[x] = value;
    x++;
  }

  mapSort1.forEach(setTimesAndValues)
  


  Chart.helpers.each(Chart.instances, function(instance){
    chart = instance.chart;
    instance.chart.destroy();
    console.log("Cleared: "+instance.chart)

  })

  var chart = new Chart(ctx, {
    // The type of chart we want to create
    type: 'line',

    // The data for our dataset
    data: {
        labels: times,
        datasets: [{
            label: document.getElementById("dropdownbtn").innerHTML,
            backgroundColor: 'rgb(255, 99, 132)',
            borderColor: 'rgb(255, 99, 132)',
            data: counter
        }]
    },

    // Configuration options go here
    options: {}
});
  
}
