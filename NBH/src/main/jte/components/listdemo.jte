@import java.util.List

@param String title
@param List<String> elements

<h2>${title}</h2>

<ul>
@for(var element : elements)
    <li>${element}</li>
@endfor
</ul>