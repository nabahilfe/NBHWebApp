@param String successMessage = null
@param String errorMessage = null

@if(successMessage != null)
    <div id="successAlert" class="alert alert-success py-2 px-3 alert-dismissible fade show" role="alert">
     	${successMessage}
  	</div>
@endif

@if(errorMessage != null)
     <div class="alert alert-danger py-2 px-3" role="alert">
     	${errorMessage}
     </div>
@endif
