<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input v-model:value="param.name" placeholder="名称">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>

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
        <!--     这里的record就是对应的一行一行的数据-->
            <a-button type="primary" @click="edit(record)">
              edit
            </a-button>
            <a-popconfirm
                title="Are you sure delete this ebook?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="del(record.id)"
            >
              <a-button type="primary" danger>
                delete
              </a-button>
            </a-popconfirm>

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
    <!--  这个ebook是专门给编辑的变量，点击编辑按钮的时候会将一行的数据设置到这个ebook里面，然后这里显示他们 -->
    <a-form :model="ebook" :label-col="{span : 6}">
      <a-form-item label="cover">
        <a-input v-model:value="ebook.cover"/>
      </a-form-item>
      <a-form-item label="name">
        <a-input v-model:value="ebook.name"/>
      </a-form-item>
      <a-form-item label="category1">
        <a-input v-model:value="ebook.category1Id"/>
      </a-form-item>
      <a-form-item label="category2">
        <a-input v-model:value="ebook.category2Id"/>
      </a-form-item>
      <a-form-item label="description">
        <a-input v-model:value="ebook.description" type="text"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { SmileOutlined, DownOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
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

    // 定义查询ebook的名字
    const param = ref()
    param.value = {}

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
          size: params.size,
          name: param.value.name
        }
      }).then((resp) => {
        loading.value = false;
        const data = resp.data;
        // data.content 会得到PageResp对象，resp对象的list才是数据
        if (data.success) {
          ebooks.value = data.content.list;
          // 重置分页按钮
          pagination.value.current = params.page;
          // 这里是后端分页查询时查询数据库的总数据量
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message)
        }
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
    // 定义了一个响应式的变量，里面的中括号表示变量是对象类型
    const ebook = ref({})
    const modalVisible = ref(false)
    const modalLoading = ref(false)
    // 点击按钮之后将修改的ebook数据保存
    const handleModalOk= () => {
      // 点击按钮之后呢显示一个loading的效果
      modalLoading.value = true;
      // 使用异步的方式保存修改的数据
      axios.post("/ebook/save", ebook.value).then((resp) => {
        // 后端只要有值返回，loading效果去掉
        modalLoading.value = false
        const data = resp.data;
        // 这里的data就是commonResp
        if (data.success) {
          // 将对话框关闭
          modalVisible.value = false
          // 重新加载列表
          handleQuery({
            // 查询当前页
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          // 保存出错后的提示
          message.error(data.message)
        }

      });
    }

    // 新增, 这个新增用到了编辑的组件，相当于弹出个无数据的编辑
    const add = () => {
      modalVisible.value = true;
      ebook.value = {}
    }

    // 在点击编辑按钮时，将那一行的数据传进edit函数，并将数据赋值给变量ebook
    const edit = (record) => {
      modalVisible.value = true;
      ebook.value = record
    }

    const del = (id) => {
      axios.delete("/ebook/delete/" + id).then((resp) => {
        const data = resp.data;
        // 这里的data就是commonResp
        if (data.success) {
          // 重新加载列表
          handleQuery({
            // 查询当前页
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        }
      });
    };

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

      param,
      edit,
      add,
      del,
      handleQuery,

      modalVisible,
      modalLoading,
      handleModalOk,
      ebook
    }
  }
});
</script>