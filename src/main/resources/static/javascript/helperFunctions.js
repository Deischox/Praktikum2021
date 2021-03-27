/**
 * The onclick Button for the Search Button. Shows the Loading Screen and calls the Function to get the Data for the Hashtag
 */
function searchResults()
{
    document.getElementById("loader").classList.toggle("d-none")
    getData(document.getElementById('dropdownbtn').innerText)
}

/**
 * Function to change the Text in the Language Dropdown Button to the selected one
 * @param {String} len - The selected Language 
 */
function changeLen(len)
{
    document.getElementById("leng").innerText = len
}

/**
 * Gets called on Page load and shows an alert if the Mysql Connection failed
 */
if(document.getElementById("yourId").value.includes("cant"))
{
    alert(document.getElementById("yourId").value)
}

/**
 * To Filter the Dropdownlist of the hashtag with the Search Terms
 */
function filterFunction() {
    var input, filter, ul, li, a, i;
    input = document.getElementById("myInput");
    filter = input.value.toUpperCase();
    div = document.getElementById("myDropdown");
    a = div.getElementsByTagName("a");
    for (i = 0; i < a.length; i++) {
      txtValue = a[i].textContent || a[i].innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        a[i].style.display = "";
      } else {
        a[i].style.display = "none";
      }
    }
    document.getElementById("dropdownbtn").innerText = filter;
  }

/**
 * Toggles the Settings Window
 */
function showSettings()
{
  document.getElementById("settingsDiv").classList.toggle("d-none")
}

/**
 * Show or hide the selected Visualisation
 * @param {String} id - Id of Element to hide/show
 */
function settingsCheckbox(id)
{
  document.getElementById(id).classList.toggle("d-none")
}

/** When the user clicks on the button,
 *  toggle between hiding and showing the dropdown content 
 */
function myFunction() {
document.getElementById("myDropdown").classList.toggle("show");
}

/**
 * For the Hashtag Select Button, so when a Hashtag gets selected the Text of the Button changes to the Hashtag
 * @param {String} hashtag - selected Hashtag
 */
function chooseHashtag(hashtag)
{
document.getElementById("myDropdown").classList.toggle("show");
document.getElementById("dropdownbtn").innerText = hashtag;
}