Vue.createApp({
    data() {
      return {
        email:"",
        password:"",

        nombre:"",
        apellido:"",
        emailregistro:"",
        contrasenaregistro:"",

        error1:false,
        error2:false
      }
    },

    created(){
    },


    methods:{     
        iniciosesion(mail,pass){
            axios.post('/api/login',`email=${mail}&password=${pass}`)
                .then(response => mail == "admin@admin.com" ? window.location.href="/web/manager.html" : window.location.href="/web/accounts.html")
                .catch( error => {
                 if(error.response.status == 401){
                   this.error1 = true
                 }
                  });
        },

        registro(){
          axios.post('/api/clients',`firstName=${this.nombre}&lastName=${this.apellido}&email=${this.emailregistro}&password=${this.contrasenaregistro}`)
          .then(response => this.iniciosesion(this.emailregistro,this.contrasenaregistro))
          .catch(error =>  {
            if(error.response.data == "Name already in use"){
              this.error2 = true
            }
            });
        },



        mostrarContrasena(){
          var tipo = document.getElementById("password");
      if(tipo.type == "password"){
          tipo.type = "text";
      }else{
          tipo.type = "password";
      }
        }
    
      },
    

  }).mount('#app')