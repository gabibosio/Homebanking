Vue.createApp({
    data() {
      return {
        tipo:"",
        color:"",

        cliente:""
      }
    },

    created(){
      axios.get("http://localhost:8080/api/clients/current").then(response =>{
        this.cliente = response.data.firstName + " " + response.data.lastName
      })
    },


    methods:{     
        creartarjeta(){
            axios.post('/api/clients/current/cards',`type=${this.tipo}&color=${this.color}`,{headers:{'content-type': 'application/x-www-form-urlencoded'}})
            .then(response => window.location.href="http://localhost:8080/web/cards.html")
            .catch(error => {
              if(error.response.data == "you have 3 cards of this type"){
                Swal.fire({
                  icon: 'error',
                  title: 'Error',
                  text: 'Ya tiene 3 Tarjetas de este tipo!',
                })
              }
              if(error.response.data == "you have 1 card of this color"){
                Swal.fire({
                  icon: 'error',
                  title: 'Error',
                  text: 'Ya tiene 1 Tarjeta de este color!',
                })
              }
              
            })
        }
    }
    

  }).mount('#app')