Vue.createApp({
    data() {
      return {
        tipo:"",
        color:"",

        cliente:""
      }
    },

    created(){
      axios.get("/api/clients/current").then(response =>{
        this.cliente = response.data.firstName + " " + response.data.lastName
      })
    },


    methods:{     
        creartarjeta(){
            axios.post('/api/clients/current/cards',`type=${this.tipo}&color=${this.color}`)
            .then(response => window.location.href="/web/cards.html")
            .catch(error => {
               Swal.fire('invalid transaction!' , error.response.data, 'error')
              
            })
        }
    }
    

  }).mount('#app')