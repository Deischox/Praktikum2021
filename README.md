<!-- PROJECT LOGO -->
<br />
<p align="center">
  <h3 align="center">Hashtag Analyse - Base.camp WS 2020/21</h3>
  <div align="center"><a style="text-align:center;display:block;" href="http://basecamp-demos.informatik.uni-hamburg.de:8080/praktikum-1/hello"> Demo </a></div>
  <img src="https://github.com/Deischox/Praktikum2021/blob/master/src/main/resources/images/header.JPG">
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

This project was programmed during the winter semester in the base.camp module. The goal was to create a website where you can enter a hashtag and then the most used words with this hashtag are visualized. The module lasted 3 weeks and the project was implemented by Jeremy, Selen, Artiom and Silas. Jeremy and Selen were responsible for analyzing the data and making it smaller. Selen set up the database and wrote the Java function to retrieve the data from it. Silas was responsible for the front- and backend.

### Built With

* [Spring-Boot](https://spring.io/projects/spring-boot)
* [Thymeleaf](https://www.thymeleaf.org/)
* [Bootstrap](https://getbootstrap.com/docs/5.0/getting-started/introduction/)
* [particles.js](https://vincentgarreau.com/particles.js/)
* [chart.js](https://www.chartjs.org/)
* [d3.js](https://d3js.org/)



<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple steps.

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/Deischox/Praktikum2021.git
   ```
2. Change the credential File (src\main\resources\credentials.txt)
   ```sh
   mysql.user= DATABASE_USERNAME
   mysql.password= DATABASE_PASSWORD
   mysql.database= DATABASE_NAME
   mysql.host= DATABASE_HOST
   ```
3. Run with maeven
   ```sh
   mvn clean spring-boot:run
   ```



<!-- USAGE EXAMPLES -->
## Usage

<img src="https://github.com/Deischox/Praktikum2021/blob/master/src/main/resources/images/settings.JPG">
</br>
To use the website, the first thing you should do is choose a hashtag. There is a selection of 200 hashtags, but you can also choose your own hashtag on the off chance. In the database there are 60000 thousand hashtags and in the dropdown there are just 0.3% of them. Next, you can select the language in which the hashtags should be analyzed. The drop checkboxes allow you to filter RT, filter @ or return only Latin characters to filter punctuation, emojis, etc.
</br>
</br>
<img src="https://github.com/Deischox/Praktikum2021/blob/master/src/main/resources/images/settingstab.JPG">
</br>
In the settings you can filter the types of visualization. So if you don't want to have the Wordcloud, you can simply hide it.

<!-- CONTACT -->
## Contact

Silas Ueberschaer - <a href= "mailto:silas.ueberschaer@gmx.de">Send Email</a>


Project Link: [Github](https://github.com/Deischox/Praktikum2021)



<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements

* []()
* []()
* []()





<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo.svg?style=for-the-badge
[contributors-url]: https://github.com/github_username/repo/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/github_username/repo.svg?style=for-the-badge
[forks-url]: https://github.com/github_username/repo/network/members
[stars-shield]: https://img.shields.io/github/stars/github_username/repo.svg?style=for-the-badge
[stars-url]: https://github.com/github_username/repo/stargazers
[issues-shield]: https://img.shields.io/github/issues/github_username/repo.svg?style=for-the-badge
[issues-url]: https://github.com/github_username/repo/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo.svg?style=for-the-badge
[license-url]: https://github.com/github_username/repo/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/github_username
