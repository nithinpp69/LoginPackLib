# LoginPackLib
Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
  
  Add the dependency
```  
  dependencies {
	        implementation 'com.github.jubinSayone:LoginPackLib:v1.0.0'
	}
  ```
  For Login (Without any dialog popup after login api call)
  ```
   new LoginPack.Builder()
                .api() // login api
                .packageName()
                .callback(this)
                .delegate(this)
                .loginLayout()// layout xml of login page
                .mainLayout() // layout xml of main layout
                .mainLayoutId() // id of main layout
                .emailFieldId() // id of email input field
                .passwordFieldId()// id of password input filed
                .submitButtonId()// id of submit button
                .build();
  ```      
  
  For Login (With dialog popup for success or failure) :
  
  ``` 
  (.isDialogue(true),.failureLayout(),.successLayout() need to be added only when dialog popup is required after login else exclude this as in the above case)
  
  
       new LoginPack.Builder()
                .api(api url)
                .packageName("com.example.myapplication")
                .callback(this)
                .delegate(this)
                .loginLayout(R.layout.login_layout)
                .mainLayout(R.layout.activity_main)
                .mainLayoutId("layout")
                .emailFieldId("edtEmail")
                .passwordFieldId("edtPassword")
                .submitButtonId("btnSubmit")
                .isDialogue(true)// should be true,only when popup dialogue required after login
                .failureLayout(R.layout.failed_layout) // layout of the failure dialog
                .successLayout(R.layout.sucess_layout) // layout of the success dialog
                .build();
             
  ``` 
   
