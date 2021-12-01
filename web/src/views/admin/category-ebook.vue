<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

      <p>
        <a-form layout="inline">
          <a-form-item>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery()">
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
               :data-source="tree_data"
               :pagination="false"
               :loading="loading"

      >

        <!--   a-space 空格的组件     -->
        <template v-slot:action="{ text, record }">
          <a-space size="small">
        <!--     这里的record就是对应的一行一行的数据-->
            <a-button type="primary" @click="edit(record)">
              edit
            </a-button>
            <a-popconfirm
                title="Are you sure delete this category?"
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
    <!--  这个category是专门给编辑的变量，点击编辑按钮的时候会将一行的数据设置到这个category里面，然后这里显示他们 -->
    <a-form :model="category" :label-col="{span : 6}">
      <a-form-item label="name">
        <a-input v-model:value="category.name"/>
      </a-form-item>

      <a-form-item label="parent">
        <a-select v-model:value="category.parent" ref="select">
          <a-select-option value="0">none</a-select-option>
          <a-select-option v-for="c in tree_data" :key="c.id" value="c.id" :disabled="category.id===c.id">{{c.name}}</a-select-option>
        </a-select>
      </a-form-item>

      <a-form-item label="sort">
        <a-input v-model:value="category.sort"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script>
import { SmileOutlined, DownOutlined } from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import { defineComponent, onMounted, ref } from 'vue';
import axios from "axios";
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminCategory',
  components: {
    SmileOutlined,
    DownOutlined,
  },
  setup() {
    const categorys = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
      total: 0
    });

    const loading = ref(false);
    const columns = [
      {
        title: 'name',
        dataIndex: 'name',
      },
      {
        title: 'parent',
        key: 'parent',
        dataIndex: 'parent'  // 这里应该时和数据库的名称对应
      },
      {
        title: 'sort',
        key: 'sort',
        dataIndex: 'sort'
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}  // 这里是渲染
      },
    ];



    // 查询数据按钮
    const tree_data = ref()

    const handleQuery = () => {
      loading.value = true;
      axios.get("/category/all").then((resp) => {
        loading.value = false;
        const data = resp.data;
        // data.content 会得到PageResp对象，resp对象的list才是数据
        if (data.success) {
          categorys.value = data.content;
          // 重置分页按钮
          tree_data.value = []
          tree_data.value = Tool.array2Tree(categorys.value, 0)
          console.log("tree structe", tree_data)
        } else {
          message.error(data.message)
        }
      });
    };


    // 定义了一个响应式的变量，里面的中括号表示变量是对象类型
    const category = ref({})
    const modalVisible = ref(false)
    const modalLoading = ref(false)
    // 点击按钮之后将修改的category数据保存
    const handleModalOk= () => {
      // 点击按钮之后呢显示一个loading的效果
      modalLoading.value = true;
      // 使用异步的方式保存修改的数据
      axios.post("/category/save", category.value).then((resp) => {

        const data = resp.data;
        // 这里的data就是commonResp
        if (data.success) {
          // 将对话框关闭
          modalVisible.value = false;
          // 拿到值之后将loading效果去掉
          modalLoading.value = false

          // 重新加载列表
          handleQuery();
        } else {
          // 保存出错后的提示
          message.error(data.message)
          modalLoading.value = false
        }

      });
    }

    // 新增, 这个新增用到了编辑的组件，相当于弹出个无数据的编辑
    const add = () => {
      modalVisible.value = true;
      category.value = {}
    }

    // 在点击编辑按钮时，将那一行的数据传进edit函数，并将数据赋值给变量category
    const edit = (record) => {
      modalVisible.value = true;
      // 这里直接把record传递到category，编辑时会直接影响原值，即使没有提交。
      category.value = Tool.copy(record)
    }

    const del = (id) => {
      axios.delete("/category/delete/" + id).then((resp) => {
        const data = resp.data;
        // 这里的data就是commonResp
        if (data.success) {
          // 重新加载列表
          handleQuery();
        }
      });
    };

    // 打开页面时查询数据
    onMounted(() => {
      handleQuery();
    })

    return {
      categorys,
      pagination,
      columns,
      loading,
      tree_data,

      edit,
      add,
      del,
      handleQuery,

      modalVisible,
      modalLoading,
      handleModalOk,
      category
    }
  }
});
</script>