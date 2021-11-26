<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
<!--    :row-key="record => record.id" 每一行都要给一个key
        :pagination="pagination" 定义了一个pagination变量
        :loading="loading" 用到了loading变量
        @change="handleTableChange" 点击分页会执行方法

  -->
      <a-table :columns="columns"
               :row-key="record => record.id"
               :data-source="ebooks"
               :pagination="pagination"
               :loading="loading"
               @change="handleTableChange"
      >
        <!--   渲染封面, 对应setup里面的      -->
        <template #cover="{text: cover}">
          <img v-if="cover" :src="cover" alt="avator" style="width: 50px; height: 50px">
        </template>
        <!--   a-space 空格的组件     -->
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit">
              编辑
            </a-button>
            <a-button type="primary" danger>
              删除
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
<!--  这个是弹出来的对话框， 跟在按钮后面即可-->
  <a-modal
      v-model:visible="modalVisible"
      title="Title"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <p>test</p>
  </a-modal>
</template>

<script>
import { SmileOutlined, DownOutlined } from '@ant-design/icons-vue';

import { defineComponent, onMounted, ref } from 'vue';
import axios from "axios";

export default defineComponent({
  name: 'AdminEbook',
  components: {
    SmileOutlined,
    DownOutlined,
  },
  setup() {
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });
    const loading = ref(false);
    const columns = [
      {
        title: 'cover',
        dataIndex: 'cover',
        slots: {customRender: 'cover' } // 这里的封面有个渲染
      },
      {
        title: 'name',
        dataIndex: 'name',
      },
      {
        title: 'category1',
        key: 'category1Id',
        dataIndex: 'category1Id'  // 这里应该时和数据库的名称对应
      },
      {
        title: 'category2',
        key: 'category2Id',
        dataIndex: 'category2Id'
      },
      {
        title: 'document count',
        dataIndex: 'docCount'
      },
      {
        title: 'view count',
        dataIndex: 'viewCount'
      },
      {
        title: 'vote count',
        dataIndex: 'voteCount'
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}  // 这里是渲染
      },
    ];
    // 查询数据按钮
    const handleQuery = (params) => {
      loading.value = true;
      axios.get("/ebook/list", {
        params: {
          page: params.page,
          size: params.size
        }
      }).then((resp) => {
        loading.value = false;
        const data = resp.data;
        // data.content 会得到PageResp对象，resp对象的list才是数据
        ebooks.value = data.content.list;

        // 重置分页按钮
        pagination.value.current = params.page;
        // 这里是后端分页查询时查询数据库的总数据量
        pagination.value.total = data.content.total;
      });
    };

    // 点击表格页码的时候触发
    const handleTableChange = (pagination) => {
      console.log("自带分页参数:" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };
    const modalVisible = ref(false)
    const modalLoading = ref(false)
    const handleModalOk= () => {
      modalLoading.value = true;
      setTimeout(() => {
        modalVisible.value = false
        modalLoading.value = false
      }, 2000)
    }

    const edit = () => {
      modalVisible.value = true;
    }

    // 打开页面时查询数据
    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    })

    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      edit,
      modalVisible,
      modalLoading,
      handleModalOk
    }
  }
});
</script>