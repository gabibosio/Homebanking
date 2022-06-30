Vue.createApp({
    data() {
      return {
        tipo:"",
        color:"",

        cliente:""
      }
    },

    created(){
      axios.get("https://homebakingmindhub.herokuapp.com/api/clients/current").then(response =>{
        this.cliente = response.data.firstName + " " + response.data.lastName
      })
    },


    methods:{     
        creartarjeta(){
            axios.post('https://homebakingmindhub.herokuapp.com/api/clients/current/cards',`type=${this.tipo}&color=${this.color}`,{headers:{'content-type': 'application/x-www-form-urlencoded'}})
            .then(response => window.location.href="https://homebakingmindhub.herokuapp.com/web/cards.html")
            .catch(error => {
               Swal.fire('invalid transaction!' , error.response.data, 'error')
              
            })
        }
    }
    

  }).mount('#app')