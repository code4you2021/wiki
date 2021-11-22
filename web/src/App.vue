<template>
  <a-layout>
    <theHeader/>
      <router-view/>
    <theFooter/>
  </a-layout>
</template>

<script>
// @ is an alias to /src
import theHeader from '@/components/the-header'
import theFooter from '@/components/the-footer'

export default {
  name: 'App',
  components: {
    theHeader, theFooter
  }
}
</script>


