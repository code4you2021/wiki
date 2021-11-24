<template>
  <a-layout-header class="header">
    <!--      <div class="logo" />-->
    <a-menu

        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="1">
        <router-link to="/">Home</router-link>
      </a-menu-item>
      <a-menu-item key="2">
        <router-link to="/admin/ebook">Ebook</router-link>
      </a-menu-item>
      <a-menu-item key="3">
        <router-link to="/about">About</router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script>
export default {
  name: 'the-header',
}
</script>