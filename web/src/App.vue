<template>
  <a-layout>
    <theHeader/>
      <router-view/>
    <a-layout-footer style="text-align: center">
      Roc ©2021 Created by Roc
    </a-layout-footer>
  </a-layout>
</template>

<script>
// @ is an alias to /src
import theHeader from '@/components/the-header'

export default {
  name: 'App',
  components: {
    theHeader
  }
}
</script>


